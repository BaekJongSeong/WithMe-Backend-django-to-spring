package com.server.withme.serivce;

import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

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
	
	public AccountOption findByAccountIdOrThrow(UUID accountId);
}
