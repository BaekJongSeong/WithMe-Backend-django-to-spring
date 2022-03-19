package com.server.withme.serivce.impl;

import org.springframework.stereotype.Service;

import com.server.withme.entity.AccountOption;
import com.server.withme.model.SignupDto;
import com.server.withme.repository.AccountOptionRepository;
import com.server.withme.serivce.IAccountOptionService;

import lombok.RequiredArgsConstructor;

/**
 * Service for AccountOptionService
 *
 * @author Jongseong Baek
 */

@RequiredArgsConstructor
@Service
public class AccountOptionService implements IAccountOptionService{

	private final AccountOptionRepository accountOptionRepository;
	
	@Override
	public AccountOption signUpOption(SignupDto signupDto) {
		AccountOption accountOption = AccountOption.builder()
										.boxSize(100)
										.distance(0.0)
										.initSafeZone(true)
										.safeMove(false)
										.xPoint(null)
										.yPoint(null)
										.build();
		return accountOptionRepository.save(accountOption);
	}
}
