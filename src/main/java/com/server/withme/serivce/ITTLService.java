package com.server.withme.serivce;
import java.util.Date;
import java.util.List;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.TTL;
import com.server.withme.model.TTLDto;
import com.server.withme.model.VertexDto;
/**
 * Interface for TTLService
 *
 * @author Jongseong Baek
 */
public interface ITTLService {
	
	public void saveTTLFirstTime(List<VertexDto> initSafeZoneList, AccountOption accountOption);
	
	public TTL saveTTL(AccountOption accountOption);
	
	public TTL ttlUpdate(TTL ttl, int index);
		
	public void deleteAllTTLById(List<Integer> ttlIdList);
	
	public TTLDto createTTLDto(Date date);
	
	public TTL createTTLEntity(Date date, AccountOption accountOption);
	
	public TTL findByTTLIdOrThrow(int index);
	
	public List<TTL> findByAccountOptionIdOrThrow(Integer accountOptionId);
}
