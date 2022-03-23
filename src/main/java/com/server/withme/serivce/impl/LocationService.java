package com.server.withme.serivce.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.Location;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.LocationDto;
import com.server.withme.repository.LocationRepository;
import com.server.withme.repository.SafeZoneRepository;
import com.server.withme.repository.TTLRepository;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.ILocationService;
import com.server.withme.util.IVertexUtil;

import lombok.RequiredArgsConstructor;
/**
 * Service for Location
 *
 * @author Jongseong Baek
 */

@RequiredArgsConstructor
@Service
public class LocationService implements ILocationService{
	
	private final IVertexUtil vertexUtil;
	
	private final LocationRepository locationRepository;
	
	private final TTLRepository ttlRepository;
	
	private final SafeZoneRepository safeZoneRepository;
	
	private final IAccountOptionService accountOptionService;
	
	
	@Override
	public Map<String,Boolean> saveLocation(LocationDto locationDto, UUID accountId) {
		
		Map<String,Boolean> map = new HashMap<>();
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		Location location = locationRepository.findByJoinFetchLatest(accountOption.getId());
		
		if(!vertexUtil.checkLatestLocation(locationDto,location)) {
			map.put("latest", false);
			return map;
		}
		locationRepository.save(Location.builder()
					.timestamp(locationDto.getTimestamp())
					.latitude(locationDto.getVertexDto().getLatitude())
					.longitude(locationDto.getVertexDto().getLongitude())
					.accountOption(accountOption)
					.build());
		
		if(this.checkInAndOut(locationDto,accountOption))
			map.put("inAndOut", true);
		else
			map.put("inAndOut", false);
		
		map.put("latest", true);
		return	map;
	}
	
	@Override
	public boolean checkInAndOut(LocationDto locationDto,AccountOption accountOption) {
		
		List<TTL> ttlList= ttlRepository.findByJoinFetch(accountOption.getId());
		List<List<SafeZone>> totalSafeZoneList = new ArrayList<>();
		for(TTL ttl : ttlList) 
			totalSafeZoneList.add(safeZoneRepository.findByJoinFetch(ttl.getId()));
		
		List<SafeZone> safeZoneList  =vertexUtil.checkInAndOutLocation(SafeZone.builder()
										.latitude(locationDto.getVertexDto().getLatitude())
										.longitude(locationDto.getVertexDto().getLongitude()).build(), totalSafeZoneList, ttlList);
		
		double result = safeZoneList.get(safeZoneList.size()-1).getLatitude();
		if(result==1.0)
			return true;
		
		return false;
	}
}
