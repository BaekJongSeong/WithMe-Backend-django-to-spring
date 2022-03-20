package com.server.withme.serivce.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.TTL;
import com.server.withme.model.AccountIdDto;
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
	
	private final TTLRepository ttlRepository;
	
	private final IVertexUtil vertexUtil;
	
	@SuppressWarnings("static-access")
	@Override
	public void saveTTL(AccountIdDto accountIdDto) {
		
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountIdDto.getAccountId());
		Integer count = vertexUtil.countSafeZone(accountOption);
		
		Timestamp Timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		
		for(int idx=0; idx< count; idx++) {
			ttlRepository.save(TTL.builder()
						.ttl(Timestamp.valueOf(sdf.format(Timestamp)))
						.accountOption(accountOption).build());
		}
	}
}
