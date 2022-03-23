package com.server.withme.serivce;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.server.withme.entity.Account;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.LoginDto;
import com.server.withme.model.SignupDto;
/**
 * Interface for AccountService
 *
 * @author Jongseong Baek
 */
public interface IAccountService extends UserDetailsService {

	// 메소드는 한번에 기능 하나만. signup 하기 위해서 체크
    public Account checkForSignup(SignupDto signupDto);

    // 메소드는 한번에 기능 하나만. signup 하기 위해서 create 기능
    public Account createAccount(SignupDto signupDto, String role);
    
    public Account modifyAccount(AccountIdDto accountIdDto);
    
    public AccountIdDto getAccountId(LoginDto loginDto);

    public Account findByAccountIdOrThrow(UUID accountId);
    
    public Account findByUsernameOrThrow(String username);
}
