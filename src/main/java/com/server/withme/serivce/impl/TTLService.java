package com.server.withme.serivce.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.TTL;
import com.server.withme.enumclass.DayCalculator;
import com.server.withme.model.VertexDto;
import com.server.withme.repository.AccountOptionRepository;
import com.server.withme.repository.TTLRepository;
import com.server.withme.serivce.ITTLService;
import com.server.withme.util.IVertexCheckUtil;

import lombok.RequiredArgsConstructor;
/**
 * Service for TTL
 *
 * @author Jongseong Baek
 */
@RequiredArgsConstructor
@Service
public class TTLService implements ITTLService{
		
	private final AccountOptionRepository accountOptionRepository;
	
	private final TTLRepository ttlRepository;
	
	private final IVertexCheckUtil vertexCheckUtil;
	
	
	@Override
	public void saveTTLFirstTime(List<VertexDto> initSafeZoneList, AccountOption accountOption) {
			Integer count = vertexCheckUtil.countSafeZone(initSafeZoneList);
			for(int idx=0; idx< count; idx++)
				ttlRepository.save(TTL.createTTLEntity(
						DayCalculator.SEVEN.calculateDay(new Timestamp(System.currentTimeMillis())).getTime(),
						accountOption));
	}
	
	@Override 
	public TTL saveTTL(AccountOption accountOption) {
		return ttlRepository.save(TTL.createTTLEntity(
				DayCalculator.ONE.calculateDay(new Timestamp(System.currentTimeMillis())).getTime(),
				accountOption));
	}
	
	@Override
	public TTL ttlUpdate(TTL ttl, int index) {
		TTL ttlOld = this.findByTTLIdOrThrow(index);
		ttlOld.setTtl(DayCalculator.ONE.calculateDay(ttlOld.getTtl()).getTime());
		return ttlRepository.save(ttlOld);
	}
	
	@Override
	public void deleteAllTTLById(List<Integer> ttlIdList){
		for(Integer ttlId : ttlIdList)
			ttlRepository.deleteById(ttlId);
	}

	@Override
	public TTL findByTTLIdOrThrow(int index){
		return ttlRepository.findById(index).orElseThrow(() 
        		-> new UsernameNotFoundException("not found ttl"));
	}	
	
	@Override
	public List<TTL> findByAccountOptionIdOrThrow(Integer accountOptionId){
		AccountOption accountOption= accountOptionRepository.findByFetchTTL(accountOptionId).orElseThrow(()
							-> new UsernameNotFoundException("not found accountOption"));
	
		return accountOption.getTtlList();
	}
}
