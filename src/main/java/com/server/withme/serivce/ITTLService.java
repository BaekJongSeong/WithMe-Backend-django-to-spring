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
	
	public void saveTTL(AccountIdDto accountIdDto);
	
	public void ttlUpdate(TTL ttl, int index);
	
	public Timestamp calculateTimestamp(Date timestamp, int index);
	
	public List<TTL> findByAccountOptionIdOrThrow(Integer accountOptionId);
}
