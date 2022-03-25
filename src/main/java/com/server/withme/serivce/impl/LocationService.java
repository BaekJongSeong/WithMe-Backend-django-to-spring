package com.server.withme.serivce.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.tomcat.jni.Time;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.Location;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.LocationDto;
import com.server.withme.repository.AccountOptionRepository;
import com.server.withme.repository.LocationRepository;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.ILocationService;
import com.server.withme.serivce.ISafeZoneService;
import com.server.withme.serivce.ITTLService;
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
	
	private final ITTLService ttlService;
	
	private final ISafeZoneService safeZoneService;
	
	private final IAccountOptionService accountOptionService;
	
	private final AccountOptionRepository accountOptionRepository;
	
	@Override
	public boolean checkLatestLocation(LocationDto locationDto , UUID accountId) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		Location location = locationRepository.findByFetchLatest(accountOption.getId());
		return vertexUtil.checkLatestLocation(locationDto,location);
	}
	
	@Override
	public boolean saveLocation(LocationDto locationDto, UUID accountId) {
		
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		Location location =Location.builder()
					.timestamp(locationDto.getTtlDto().getTtl())
					.latitude(locationDto.getVertexDto().getLatitude())
					.longitude(locationDto.getVertexDto().getLongitude())
					.accountOption(accountOption)
					.build();
		
		if(!this.checkLatestLocation(locationDto,accountId))
			return false;
		
		locationRepository.save(location);
		return true;
	}
	
	@Override
	public Map<String,Boolean> checkInAndOut(LocationDto locationDto, UUID accountId) {
		Map<String,Boolean> map = new HashMap<>();
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
		List<List<SafeZone>> totalSafeZoneList = new ArrayList<>();
		
		for(TTL ttl : ttlList) 
			totalSafeZoneList.add(safeZoneService.findByTTLIdOrThrow(ttl.getId()));
		
		List<SafeZone> safeZoneList  =vertexUtil.checkInAndOutLocation(SafeZone.builder()
										.latitude(locationDto.getVertexDto().getLatitude())
										.longitude(locationDto.getVertexDto().getLongitude()).build(), totalSafeZoneList, ttlList);
		
		double result = safeZoneList.remove(safeZoneList.size()-1).getLatitude();
		System.out.println("result: "+result +": "+ safeZoneList.toString());
		
		if(result==1.0)
			map.put("inAndOut", true);
		else
			map.put("inAndOut", false);
		
		return map;
	}
	
	@Override
	public List<Location> findByAccountOptionIdOrThrow(Integer accountOptionId) {
		AccountOption accountOption = accountOptionRepository.findByFetchLocation(accountOptionId).orElseThrow(() 
        		-> new UsernameNotFoundException("not found accountOption"));
	
		return accountOption.getLoctionList();
	}
}
