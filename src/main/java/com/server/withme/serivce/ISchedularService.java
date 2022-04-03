package com.server.withme.serivce;

import java.util.List;

import com.server.withme.entity.Account;
/**
 * Interface for SchedularService
 *
 * @author Jongseong Baek
 */
public interface ISchedularService {
	
	public void updateSafeZoneBySchedular(List<Account> checkedAccountList);

}
