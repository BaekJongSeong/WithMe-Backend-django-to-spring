package com.server.withme.serivce.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.Location;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.LocationDto;
import com.server.withme.model.VertexDto;
import com.server.withme.repository.AccountOptionRepository;
import com.server.withme.repository.LocationRepository;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.ILocationService;
import com.server.withme.serivce.ISafeZoneService;
import com.server.withme.serivce.ITTLService;
import com.server.withme.util.IVertexCheckUtil;

import lombok.RequiredArgsConstructor;
/**
 * Service for Location
 *
 * @author Jongseong Baek
 */

@RequiredArgsConstructor
@Service
public class LocationService implements ILocationService{
		
	private final LocationRepository locationRepository;
				
	private final AccountOptionRepository accountOptionRepository;

	private final IVertexCheckUtil vertexCheckUtil;
	
	@Override
	public boolean checkLatestLocation(LocationDto locationDto , AccountOption accountOption) {
		Location location = locationRepository.findByAccountOption_Id(accountOption.getId());
		return vertexCheckUtil.checkLatestLocation(locationDto,location);
	}
	
	@Override
	public VertexDto saveLocation(LocationDto locationDto, AccountOption accountOption) {
		Location location = Location.createLocationEntity(locationDto, accountOption);
		locationRepository.save(location);
		return new VertexDto(locationDto.getVertexDto().getLatitude(),
				locationDto.getVertexDto().getLongitude(),this.checkLatestLocation(locationDto,accountOption));
	}
	
	@Override
	public VertexDto checkInAndOut(LocationDto locationDto, AccountOption accountOption,List<TTL> ttlList,List<List<SafeZone>> totalSafeZoneList) {		
		Boolean TF  =vertexCheckUtil.checkInAndOutLocation(SafeZone.builder()
				.latitude(locationDto.getVertexDto().getLatitude())
				.longitude(locationDto.getVertexDto().getLongitude()).build(), totalSafeZoneList, ttlList);

		return new VertexDto(locationDto.getVertexDto().getLatitude(),
				locationDto.getVertexDto().getLongitude(), TF);
	}
	
	@Override
	public List<Location> findByAccountOptionIdOrThrow(Integer accountOptionId) {
		AccountOption accountOption = accountOptionRepository.findByFetchLocation(accountOptionId).orElseThrow(() 
        		-> new UsernameNotFoundException("not found accountOption"));
	
		return accountOption.getLocationList();
	}
}
