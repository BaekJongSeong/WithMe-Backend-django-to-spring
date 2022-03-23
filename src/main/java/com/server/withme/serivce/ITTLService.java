package com.server.withme.serivce;

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
}
