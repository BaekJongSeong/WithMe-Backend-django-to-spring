package com.server.withme.serivce.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.server.withme.entity.Account;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.LoginDto;
import com.server.withme.model.SignupDto;
import com.server.withme.repository.AccountRepository;
import com.server.withme.serivce.IAccountService;

import lombok.RequiredArgsConstructor;

/**
 * Service for AccountService
 *
 * @author Jongseong Baek
 */

@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    
	@Override
    public UserDetails loadUserByUsername(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() 
        		-> new UsernameNotFoundException("not found username : " + username));

        return Account.builder()
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .timestamp(account.getTimestamp())
                .name(account.getName())
                .email(account.getEmail())
                .build();
    }
	
	@Override
	public Account checkForSignup(SignupDto signupDto) {	
	    return accountRepository.findByUsername(signupDto.getUsername())
	    		.orElse(this.createAccount(signupDto, "USER"));
	}

	
	@Override
	public Account createAccount(SignupDto signupDto, String role) {
		Timestamp Timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

	    @SuppressWarnings("static-access")
		Account newAccount = Account.builder()
	    			.timestamp(Timestamp.valueOf(sdf.format(Timestamp)))
	                .username(signupDto.getUsername())
	                .password(passwordEncoder.encode(signupDto.getPassword()))
	                .name(signupDto.getName())
	                .email(signupDto.getEmail())
	                .emailVerified(false)
	                .unLocked(false)
	                .accountType(role).build();

	    return accountRepository.save(newAccount);
	}
	
	@Override
	public Account modifyAccount(AccountIdDto accountIdDto) {
		Account account = accountRepository.findByAccountId(accountIdDto.getAccountId())
				.orElseThrow(() -> new UsernameNotFoundException("not found user"));
		
		Account modifyAccount = Account.builder()
		.timestamp(account.getTimestamp())
        .username(account.getUsername())
        .password(account.getPassword())
        .name(account.getName())
        .email(account.getEmail())
        .emailVerified(true)
        .unLocked(true)
        .accountType(account.getAccountType()).build();
		
		return accountRepository.save(modifyAccount);
	}
	
	@Override
	public AccountIdDto getAccountId(LoginDto loginDto) {
		Account account = accountRepository.findByUsername(loginDto.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("not found user"));
		
		return AccountIdDto.builder()
				.accountId(account.getAccountId())
				.build();
	}
	
}
