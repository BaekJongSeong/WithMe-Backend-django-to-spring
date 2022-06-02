package com.server.withme.serivce;

import java.util.List;

import com.server.withme.entity.Account;
import com.server.withme.entity.AccountOption;
/**
 * Interface for SchedularService
 *
 * @author Jongseong Baek
 */
public interface ISchedularService {
	
	public void updateSafeZoneBySchedular(List<AccountOption> accountOptionList);
	
	public void deleteExpireTTLBySchedular(List<AccountOption> accountOptionList);
}
