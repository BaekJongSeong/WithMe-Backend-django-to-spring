package com.server.withme.serivce.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

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
 * Service for Account
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
        Account account = this.findByUsernameOrThrow(username);
        
        return Account.builder()
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .timestamp(account.getTimestamp())
                .name(account.getName())
                .email(account.getEmail()).build();
    }
	
	@Override
	public Account checkForSignup(SignupDto signupDto) {
	    return accountRepository.findByUsername(signupDto.getLoginDto().getUsername())
	    		.orElseGet((()-> this.createAccount(signupDto, "USER")));
	}

	
	@Override
	public Account createAccount(SignupDto signupDto, String role) {
		Timestamp Timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

	    @SuppressWarnings("static-access")
		Account newAccount = Account.builder()
	    			.timestamp(Timestamp.valueOf(sdf.format(Timestamp)))
	                .username(signupDto.getLoginDto().getUsername())
	                .password(passwordEncoder.encode(signupDto.getLoginDto().getPassword()))
	                .name(signupDto.getName())
	                .email(signupDto.getEmail())
	                .emailVerified(false)
	                .unLocked(false)
	                .accountType(role).build();

	    return accountRepository.save(newAccount);
	}
	
	@Override
	public Account modifyAccount(AccountIdDto accountIdDto) {
		Account account = this.findByAccountIdOrThrow(accountIdDto.getAccountId());
		account.setEmailVerified(true);
		account.setUnLocked(true);
		
		return accountRepository.save(account);
	}
	
	@Override
	public AccountIdDto getAccountId(LoginDto loginDto) {
		Account account = this.findByUsernameOrThrow(loginDto.getUsername());
		
		return AccountIdDto.builder()
				.accountId(account.getAccountId()).build();
	}
	
	 @Override
	    public Account findByAccountIdOrThrow(UUID accountId){
	    	Account account = accountRepository.findByAccountId(accountId).orElseThrow(() 
	        		-> new UsernameNotFoundException("not found user"));
	    	
	    	return account;
	    }
	    
	    @Override
	    public Account findByUsernameOrThrow(String username){
	    	Account account = accountRepository.findByUsername(username).orElseThrow(() 
	        		-> new UsernameNotFoundException("not found username : " + username));
	    	
	    	return account;
	    }
}
