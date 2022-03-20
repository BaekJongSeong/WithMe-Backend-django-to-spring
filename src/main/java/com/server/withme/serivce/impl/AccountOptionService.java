package com.server.withme.serivce.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.server.withme.entity.Account;
import com.server.withme.entity.AccountOption;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.SignupDto;
import com.server.withme.repository.AccountOptionRepository;
import com.server.withme.repository.AccountRepository;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.IAccountService;

import lombok.RequiredArgsConstructor;

/**
 * Service for AccountOption
 *
 * @author Jongseong Baek
 */

@RequiredArgsConstructor
@Service
public class AccountOptionService implements IAccountOptionService{

	private final IAccountService accountService;
	
	private final AccountRepository accountRepository;
	
	private final AccountOptionRepository accountOptionRepository;
	
	//양방향이니까 양쪽 다 들고 있어야함
	@Override
	public AccountOption signUpOption(SignupDto signupDto) {
		Account account = accountService.findByUsernameOrThrow(signupDto.getLoginDto().getUsername());
		AccountOption accountOption = AccountOption.builder()
										.boxSize(100)
										.distance(0.0)
										.initSafeZone(false)
										.safeMove(false)
										.xPoint(null)
										.yPoint(null)
										.account(account).build();
		return accountOptionRepository.save(accountOption);
	}
	
	@Override
	public AccountOption updateAccountOption(UUID accountId, Double latitude, Double longitude) {
		//먼저 uuid인 accountID로 id를 찾고 id를 사용해야지
		AccountOption accountOption = this.findByAccountIdOrThrow(accountId);
		accountOption.setInitSafeZone(true);
		accountOption.setXPoint(latitude);
		accountOption.setYPoint(longitude);
		return accountOptionRepository.save(accountOption);
	}
	
	@Override
	public AccountOption findByAccountIdOrThrow(UUID accountId) {
		Account account = accountRepository.findByJoinFetch(accountId);
	
		return account.getAccountOption();
	}
}
