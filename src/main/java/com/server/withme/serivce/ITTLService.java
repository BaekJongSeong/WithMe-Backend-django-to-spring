package com.server.withme.serivce;

import java.sql.Timestamp;
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
	
	public TTL saveTTLFirstTime(List<VertexDto> initSafeZoneList, AccountOption accountOption);
	
	public TTL saveTTL(AccountOption accountOption);
	
	public TTL ttlUpdate(TTL ttl, int index);
	
	public Timestamp calculateTimestamp(Date timestamp, int index);
	
	public void deleteAllTTLById(List<Integer> ttlIdList);
	
	public TTLDto createTTLDto(TTL ttl);
	
	public TTL findByTTLIdOrThrow(int index);
	
	public List<TTL> findByAccountOptionIdOrThrow(Integer accountOptionId);
}
