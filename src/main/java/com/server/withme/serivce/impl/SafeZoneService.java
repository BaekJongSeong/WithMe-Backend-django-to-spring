package com.server.withme.serivce.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneDto;
import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.model.VertexDto;
import com.server.withme.repository.AccountOptionRepository;
import com.server.withme.repository.InitSafeZoneRepository;
import com.server.withme.repository.SafeZoneRepository;
import com.server.withme.repository.TTLRepository;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.ILocationService;
import com.server.withme.serivce.ISafeZoneService;
import com.server.withme.serivce.ITTLService;
import com.server.withme.util.IVertexCheckUtil;
import com.server.withme.util.IVertexUtil;

import lombok.RequiredArgsConstructor;
/**
 * Service for SafeZone
 *
 * @author Jongseong Baek
 */

@RequiredArgsConstructor
@Service
public class SafeZoneService implements ISafeZoneService{
	
	private final SafeZoneRepository safeZoneRepository;
	
	private final InitSafeZoneRepository initSafeZoneRepository;
	
	private final AccountOptionRepository accountOptionRepository;
	
	private final TTLRepository ttlRepository;
	
	private final ILocationService locationService;
	
	private final ISafeZoneService safeZoneService;
	
	private final IAccountOptionService accountOptionService;
		
	private final ITTLService ttlService;
	
	private final IVertexUtil vertexUtil;
	
	private final IVertexCheckUtil vertexCheckUtil;
	
	@Override
	public VertexDto saveInitSafeZone(SafeZoneDto safeZoneDto, UUID accountId) {
		
		List<VertexDto> initSafeZoneList = safeZoneDto.getSafeZone();
		VertexDto vertexDto = vertexCheckUtil.checkSafeZoneMinSize(initSafeZoneList).get(0);
		if(vertexDto.getTF()) {
			AccountOption accountOption = accountOptionService.updateAccountOption(
					accountId,vertexDto.getLatitude(),vertexDto.getLongitude());
			
			for(VertexDto vertex: initSafeZoneList) {
				initSafeZoneRepository.save(InitSafeZone.builder()
						.latitude(vertex.getLatitude())
						.longitude(vertex.getLongitude())
						.accountOption(accountOption).build());
			}
		}
		return locationService.createVertexDto(1.0,1.0, vertexDto.getTF());
	}
	
	@Override
	public List<VertexDto> saveSafeZoneFirstTime(List<VertexDto> safeZone, UUID accountId) {
		
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
		
		int count=0;
		for(TTL ttl: ttlList) {
			while(true) {
				if(count% 4 == 0 && count != 0)
					break;
				//spring batch bulk로 create하기
				safeZoneRepository.save(SafeZone.builder()
					.latitude(safeZone.get(count).getLatitude())
					.longitude(safeZone.get(count).getLongitude())
					.ttl(ttl).build());	
				count++;
			}
		}
		return safeZone;
	}
	
	@Override
	public List<VertexDto> deleteSafeZoneFirstTime(UUID accountId) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
		
		List<SafeZone> safeZoneList = new ArrayList<>();
		for(TTL ttl: ttlList) 
			safeZoneList.addAll(safeZoneService.findByTTLIdOrThrow(ttl.getId()));
		
		List<SafeZone> deleteSafeZoneList = vertexUtil.calculateDeleteVertex(safeZoneList,accountOption);
		List<Integer> ttlIndexList = new ArrayList<>();
		for(int idx=0; idx<deleteSafeZoneList.size();idx+=4)
			ttlIndexList.add(deleteSafeZoneList.get(idx).getTtl().getId());
		
		ttlService.deleteAllTTLById(ttlIndexList);
		
		return vertexUtil.convertToVertexDto(deleteSafeZoneList);
	}
	
	@Override
	public List<VertexDto> createSafeZoneByLocation(UUID accountId,LocationDto locationDto){
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		TTL ttl = ttlService.saveTTL(accountOption);
		List<VertexDto> newSafeZoneList = vertexUtil.createSafeZoneByLocation(accountOption,locationDto);
		
		for(VertexDto safeZone: newSafeZoneList)
			safeZoneRepository.save(SafeZone.builder()
					.latitude(safeZone.getLatitude())
					.longitude(safeZone.getLongitude())
					.ttl(ttl).build());	
		
		return newSafeZoneList;
	}
	
	@Override
	public List<InitSafeZone> loadInitSafeZoneList(AccountIdDto accountIdDto){
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountIdDto.getAccountId());
		return this.findByAccountOptionIdOrThrow(accountOption.getId());
	}
	
	@Override
	public <T> SafeZoneInfoDto<T> craeteSafeZoneInfoDto(List<T> list, boolean trueOrFalse, int flag){
		SafeZoneInfoDto<T> safeZoneInfoDto = new SafeZoneInfoDto<>();
		String message="";
		
		if(trueOrFalse) 
			message = (flag==1) ? "location에 해당하는 safeZone의 TTL이 업데이트 되었습니다." : "등록이 완료되었습니다.";
		 else 
			message = (flag==1) ? "새로운 safeZone이 생성되었습니다." : "최소 size 또는 같은 위치 반복을 위반하여 등록되지 않았습니다.";
		
		safeZoneInfoDto.setMessage(message);
		safeZoneInfoDto.setData(list);
		return safeZoneInfoDto;
	}

	@Override
	public List<InitSafeZone> findByAccountOptionIdOrThrow(Integer accountOptionId){
		AccountOption accountOption= accountOptionRepository.findByFetchInitSafeZone(accountOptionId).orElseThrow(()
							-> new UsernameNotFoundException("not found accountOption"));
	
		return accountOption.getInitSafeZoneList();
	}
	
	@Override
	public List<SafeZone> findByTTLIdOrThrow(Integer ttlId){
		TTL ttl= ttlRepository.findByFetchSafeZone(ttlId).orElseThrow(()
							-> new UsernameNotFoundException("not found ttl"));
	
		return ttl.getSafeZoneList();
	}
}
