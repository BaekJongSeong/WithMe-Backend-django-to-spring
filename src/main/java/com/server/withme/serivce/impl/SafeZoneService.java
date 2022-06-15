package com.server.withme.serivce.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.server.withme.entity.Account;
import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneDto;
import com.server.withme.model.VertexDto;
import com.server.withme.repository.AccountOptionRepository;
import com.server.withme.repository.AccountRepository;
import com.server.withme.repository.InitSafeZoneRepository;
import com.server.withme.repository.SafeZoneRepository;
import com.server.withme.repository.TTLRepository;
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
	
	private final AccountRepository accountRepository;
	
	private final SafeZoneRepository safeZoneRepository;
	
	private final InitSafeZoneRepository initSafeZoneRepository;
	
	private final AccountOptionRepository accountOptionRepository;
	
	private final TTLRepository ttlRepository;
					
	private final ITTLService ttlService;
	
	private final IVertexUtil vertexUtil;
	
	private final IVertexCheckUtil vertexCheckUtil;
	
	@Transactional
	@Override
	public VertexDto saveInitSafeZone(SafeZoneDto safeZoneDto, AccountOption accountOption) {
		
		List<VertexDto> initSafeZoneList = safeZoneDto.getSafeZone();
		VertexDto vertexDto = vertexCheckUtil.checkSafeZoneMinSize(initSafeZoneList).get(0);
		if(vertexDto.getTF()) {
			//accountOption = 
			accountOptionRepository.updateQuery(vertexDto.getLatitude(),vertexDto.getLongitude(),accountOption.getId());
			for(VertexDto vertex: initSafeZoneList) 
				initSafeZoneRepository.save(InitSafeZone.createInitSafeZoneEntity(vertex,accountOption));
		}
		return new VertexDto(vertexDto.getLatitude(),vertexDto.getLongitude(), vertexDto.getTF());
	}
	
	@Override
	public void saveAll(List<SafeZone> safeZoneList, int count) {
		int localCount=0;
		while(localCount< count) {
			safeZoneRepository.save(safeZoneList.get(localCount));
			localCount++;
		}
	}
	
	@Override
	public List<VertexDto> saveSafeZoneFirstTime(List<VertexDto> safeZone, AccountOption accountOption) {		
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
		List<SafeZone> safeZoneList = new ArrayList<>();		
		int count=0;
		
		for(TTL ttl: ttlList) {
			while(true) {
				if(count% 4 == 0 && count != 0)
					break;
				//spring batch bulk로 create하기
				safeZoneList.add(SafeZone.createSafeZoneEntity(safeZone.get(count),ttl));	
				count++;
			}
		}
		int localCount=0;
		while(localCount + 1000 < count) {
			this.saveAll(safeZoneList.subList(localCount, localCount+1000), 1000);
			localCount+=1000;
		}
		this.saveAll(safeZoneList.subList(localCount, count), count-localCount);
		return safeZone;
	}
	
	@Override
	@Transactional
	public List<VertexDto> deleteSafeZoneFirstTime(AccountOption accountOption) {
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
		
		List<SafeZone> safeZoneList = new ArrayList<>();
		for(TTL ttl: ttlList) 
			safeZoneList.addAll(this.findByTTLIdOrThrow(ttl.getId()));
		
		List<InitSafeZone> initSafeZoneList= this.findByAccountOptionIdOrThrow(accountOption.getId());

		
		List<SafeZone> deleteSafeZoneList = vertexUtil.calculateDeleteVertex(safeZoneList,accountOption,initSafeZoneList);
		List<Integer> ttlIndexList = new ArrayList<>();
		for(int idx=0; idx<deleteSafeZoneList.size();idx+=4)
			ttlIndexList.add(deleteSafeZoneList.get(idx).getTtl().getId());
		
		ttlService.deleteAllTTLById(ttlIndexList);
		
		return vertexUtil.convertToVertexDto(deleteSafeZoneList);
	}
	
	@Override
	public List<VertexDto> createSafeZoneByLocation(AccountOption accountOption,LocationDto locationDto){
		TTL ttl = ttlService.saveTTL(accountOption);
		List<VertexDto> newSafeZoneList = vertexUtil.createSafeZoneByLocation(accountOption,locationDto);
		
		for(VertexDto safeZone: newSafeZoneList)
			safeZoneRepository.save(SafeZone.createSafeZoneEntity(safeZone,ttl));	
		
		return newSafeZoneList;
	}
	
	@Override
	@Transactional
	public List<InitSafeZone> loadInitSafeZoneList(AccountIdDto accountIdDto){
		Account account = accountRepository.findByFetchAccountOption(accountIdDto.getAccountId()).orElseThrow(() 
        		-> new UsernameNotFoundException("not found user"));		
		return this.findByAccountOptionIdOrThrow(account.getAccountOption().getId());
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
