package com.server.withme.serivce;

import com.server.withme.model.AccountIdDto;
/**
 * Interface for TTLService
 *
 * @author Jongseong Baek
 */
public interface ITTLService {
	
	public void saveTTL(Integer count, AccountIdDto accountIdDto);
}
