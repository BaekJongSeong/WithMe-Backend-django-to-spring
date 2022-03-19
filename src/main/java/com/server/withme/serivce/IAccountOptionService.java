package com.server.withme.serivce;

import com.server.withme.entity.AccountOption;
import com.server.withme.model.SignupDto;

/**
 * Interface for AccountService
 *
 * @author Jongseong Baek
 */
public interface IAccountOptionService {

	public AccountOption signUpOption(SignupDto signupDto);
}
