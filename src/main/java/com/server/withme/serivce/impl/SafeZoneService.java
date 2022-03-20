package com.server.withme.serivce.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.entity.SafeZone;
import com.server.withme.model.InitSafeZoneDto;
import com.server.withme.model.VertexDto;
import com.server.withme.repository.InitSafeZoneRepository;
import com.server.withme.repository.TTLRepository;
import com.server.withme.repository.SafeZoneRepository;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.ISafeZoneService;

import lombok.RequiredArgsConstructor;
/**
 * Service for SafeZone
 *
 * @author Jongseong Baek
 */

@RequiredArgsConstructor
@Service
public class SafeZoneService implements ISafeZoneService{
	
	private final IAccountOptionService accountOptionService;
	
	private final InitSafeZoneRepository initSafeZoneRepository;
	
	private final TTLRepository ttlRepository;
	
	private final SafeZoneRepository safeZoneRepository;
	
	@Override
	public void saveInitSafeZone(InitSafeZoneDto initSafeZoneDto, UUID accountId) {
		
		AccountOption accountOption = accountOptionService.updateAccountOption(accountId);
		
		List<VertexDto> initSafeZoneList = initSafeZoneDto.getInitSafeZone();
		
		for(VertexDto vertex: initSafeZoneList) {
			initSafeZoneRepository.save(InitSafeZone.builder()
					.latitude(vertex.getLatitude())
					.longitude(vertex.getLongitude())
					.accountOption(accountOption)
					.build());
		}
	}
	
	@Override
	public void saveSafeZone(InitSafeZoneDto initSafeZoneDto, UUID accountId) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		List<VertexDto> initSafeZoneList = initSafeZoneDto.getInitSafeZone();
		List<TTL> ttlList= ttlRepository.findJoinFetch(accountOption.getId());

		int count=0;
		for(TTL ttl: ttlList) {
			while(count % 4 == 0) {
				safeZoneRepository.save(SafeZone.builder()
					.latitude(initSafeZoneList.get(count).getLatitude())
					.longitude(initSafeZoneList.get(count).getLongitude())
					.ttl(ttlList.get(count/4))
					.build());	
				count++;
			}
		}
	}
}
