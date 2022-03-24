package com.server.withme.serivce;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.server.withme.entity.TTL;
import com.server.withme.model.AccountIdDto;
/**
 * Interface for TTLService
 *
 * @author Jongseong Baek
 */
public interface ITTLService {
	
	public TTL saveTTL(AccountIdDto accountIdDto);
	
	public TTL ttlUpdate(TTL ttl, int index);
	
	public Timestamp calculateTimestamp(Date timestamp, int index);
	
	public List<TTL> findByAccountOptionIdOrThrow(Integer accountOptionId);
}
