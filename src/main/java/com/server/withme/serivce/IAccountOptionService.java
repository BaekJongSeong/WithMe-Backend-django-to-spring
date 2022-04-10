package com.server.withme.serivce;

import java.util.List;
import java.util.UUID;

import com.server.withme.entity.Account;
import com.server.withme.entity.AccountOption;
import com.server.withme.model.SignupDto;

/**
 * Interface for AccountOptionService
 *
 * @author Jongseong Baek
 */
public interface IAccountOptionService {

	public AccountOption signUpOption(SignupDto signupDto);
	
	public AccountOption updateAccountOption(UUID accountId, Double latitude, Double longitude);
	
	public AccountOption updateSafeMove(UUID accountId);
	
	public List<AccountOption> findAllFetchAccountOption(List<Account> checkedAccountList);
		
	public AccountOption findByAccountIdOrThrow(UUID accountId);
}
