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
	
	private final ITTLService ttlService;
	
	private final ISafeZoneService safeZoneService;
	
	private final IAccountOptionService accountOptionService;
	
	private final AccountOptionRepository accountOptionRepository;

	private final IVertexCheckUtil vertexCheckUtil;
	
	@Override
	public boolean checkLatestLocation(LocationDto locationDto , UUID accountId) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		Location location = locationRepository.findByFetchLatest(accountOption.getId());
		return vertexCheckUtil.checkLatestLocation(locationDto,location);
	}
	
	@Override
	public VertexDto saveLocation(LocationDto locationDto, UUID accountId) {
		
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		Location location =Location.builder()
					.timestamp(locationDto.getTtlDto().getTtl())
					.latitude(locationDto.getVertexDto().getLatitude())
					.longitude(locationDto.getVertexDto().getLongitude())
					.accountOption(accountOption)
					.build();
	
		locationRepository.save(location);
		return this.createVertexDto(locationDto.getVertexDto().getLatitude(),
				locationDto.getVertexDto().getLongitude(),this.checkLatestLocation(locationDto,accountId));
	}
	
	@Override
	public VertexDto checkInAndOut(LocationDto locationDto, UUID accountId) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
		List<List<SafeZone>> totalSafeZoneList = new ArrayList<>();
		
		for(TTL ttl : ttlList) 
			totalSafeZoneList.add(safeZoneService.findByTTLIdOrThrow(ttl.getId()));
		
		Boolean TF  =vertexCheckUtil.checkInAndOutLocation(SafeZone.builder()
				.latitude(locationDto.getVertexDto().getLatitude())
				.longitude(locationDto.getVertexDto().getLongitude()).build(), totalSafeZoneList, ttlList);

		return this.createVertexDto(locationDto.getVertexDto().getLatitude(),
				locationDto.getVertexDto().getLongitude(), TF);
	}
	
	
	public VertexDto createVertexDto(double latitude, double longitude) {
		return VertexDto.builder()
		.latitude(latitude)
		.longitude(longitude)
		.build();
	}
	
	@Override
	public VertexDto createVertexDto(double latitude, double longitude, boolean TF) {
		return VertexDto.builder()
		.latitude(latitude)
		.longitude(longitude).TF(TF)
		.build();
	}
	
	@Override
	public List<Location> findByAccountOptionIdOrThrow(Integer accountOptionId) {
		AccountOption accountOption = accountOptionRepository.findByFetchLocation(accountOptionId).orElseThrow(() 
        		-> new UsernameNotFoundException("not found accountOption"));
	
		return accountOption.getLoctionList();
	}
}
