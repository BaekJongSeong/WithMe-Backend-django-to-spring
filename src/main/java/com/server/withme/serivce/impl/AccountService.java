package com.server.withme.serivce.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.server.withme.entity.Account;
import com.server.withme.enumclass.DayCalculator;
import com.server.withme.model.AccountDto;
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
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		Account newAccount = Account.builder()
	    			.timestamp(timestamp)
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
				.accountId(account.getAccountId())
				.build();
	}
	
	@Override
    public List<Account> findAllAccount(){
		return accountRepository.findAll();
	}
	
	@Override
    public List<Account> checkSevenDayOver(List<Account> accountList){
		List<Account> checkedAccountList = new ArrayList<>();
		Calendar calStandard = DayCalculator.ZERO.calculateDay(new Timestamp(System.currentTimeMillis()));
		
		for(Account account : accountList) { 
			Calendar cal = DayCalculator.SEVEN.calculateDay(account.getTimestamp());
			if(cal.getTime().getTime() < calStandard.getTime().getTime())
				checkedAccountList.add(account);
		}
		return checkedAccountList;
	}
	
	@Override
	public AccountDto createAccountDto(Account account) {
		return AccountDto.builder()
			    .timestamp(account.getTimestamp())
			    .name(account.getName())
			    .emailVerified(account.getEmailVerified())
			    .accountType(account.getAccountType()).build();
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
