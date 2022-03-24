package com.server.withme.serivce.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.TTL;
import com.server.withme.model.AccountIdDto;
import com.server.withme.repository.AccountOptionRepository;
import com.server.withme.repository.TTLRepository;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.ITTLService;
import com.server.withme.util.IVertexUtil;

import lombok.RequiredArgsConstructor;
/**
 * Service for TTL
 *
 * @author Jongseong Baek
 */
@RequiredArgsConstructor
@Service
public class TTLService implements ITTLService{
	
	private final IAccountOptionService accountOptionService;
	
	private final AccountOptionRepository accountOptionRepository;
	
	private final TTLRepository ttlRepository;
	
	private final IVertexUtil vertexUtil;
	
	@Override
	public TTL saveTTL(AccountIdDto accountIdDto) {
		
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountIdDto.getAccountId());
		Integer count = vertexUtil.countSafeZone(accountOption);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		timestamp = this.calculateTimestamp(timestamp, 7);
		
		TTL ttl = TTL.builder()
				.ttl(timestamp)
				.accountOption(accountOption).build();
		
		for(int idx=0; idx< count; idx++)
			ttlRepository.save(ttl);
		
		return ttl;
	}
	
	@Override
	public TTL ttlUpdate(TTL ttl, int index) {
		TTL ttlOld = ttlRepository.findById(index).orElseThrow(() 
        		-> new UsernameNotFoundException("not found ttl"));
		
		Timestamp timestamp = this.calculateTimestamp(ttlOld.getTtl(), 1);
		ttlOld.setTtl(timestamp);
		return ttlRepository.save(ttlOld);
	}
	
	@Override
	public Timestamp calculateTimestamp(Date timestamp, int index) {
		Calendar cal = Calendar.getInstance();
		Calendar calStandard = Calendar.getInstance();
		
		cal.setTime(timestamp);
		calStandard.setTime(new Timestamp(System.currentTimeMillis()));
		cal.add(Calendar.DATE, index);
		calStandard.add(Calendar.DATE, 7);
		
		if(cal.getTime().getTime() > calStandard.getTime().getTime())
			return Timestamp.valueOf(timestamp.toString());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		return Timestamp.valueOf(sdf.format(cal.getTime().getTime()));
	}
	
	@Override
	public List<TTL> findByAccountOptionIdOrThrow(Integer accountOptionId){
		AccountOption accountOption= accountOptionRepository.findByFetchTTL(accountOptionId).orElseThrow(()
							-> new UsernameNotFoundException("not found accountOption"));
	
		return accountOption.getTtlList();
	}
}
