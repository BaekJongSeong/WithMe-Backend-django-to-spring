package com.server.withme.serivce;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.TTL;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.TTLDto;
/**
 * Interface for TTLService
 *
 * @author Jongseong Baek
 */
public interface ITTLService {
	
	public TTL saveTTLFirstTime(AccountIdDto accountIdDto);
	
	public TTL saveTTL(AccountOption accountOption);
	
	public TTL ttlUpdate(TTL ttl, int index);
	
	public Timestamp calculateTimestamp(Date timestamp, int index);
	
	public TTLDto createTTLDto(TTL ttl);
	
	public List<TTL> findByAccountOptionIdOrThrow(Integer accountOptionId);
}
