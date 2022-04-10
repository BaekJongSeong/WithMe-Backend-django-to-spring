package com.server.withme.serivce;
import java.util.List;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.TTL;
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
	
	public TTL findByTTLIdOrThrow(int index);
	
	public List<TTL> findByAccountOptionIdOrThrow(Integer accountOptionId);
}
