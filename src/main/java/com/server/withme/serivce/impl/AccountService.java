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
        return (UserDetails) this.findByUsernameOrThrow(username);
    }
	
	@Override
	public Account checkForSignup(SignupDto signupDto) {
	    return accountRepository.findByUsername(signupDto.getLoginDto().getUsername())
	    		.orElseGet((()-> this.createAccount(signupDto, "USER")));
	}

	
	@Override
	public Account createAccount(SignupDto signupDto, String role) {
	    return accountRepository.save(Account.createAccountEntity(signupDto,
	    		passwordEncoder.encode(signupDto.getLoginDto().getPassword()),role));
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
		return AccountIdDto.builder().accountId(account.getAccountId()).build();
	}
	
	@Override
    public List<Account> findAllAccount(){
		return accountRepository.findAll();
	}
	
	@Override
    public Account findByEmail(String email) {
		return accountRepository.findByEmail(email);
	}
	
	@Override
    public List<Account> checkSevenDayOver(List<Account> accountList){
		List<Account> checkedAccountList = new ArrayList<>();
		Calendar calStandard = DayCalculator.ZERO.calculateDay(new Timestamp(System.currentTimeMillis()));
		
		for(Account account : accountList) { 
			Calendar cal = DayCalculator.SEVEN.calculateDay(account.getCreateAt());
			if(cal.getTime().getTime() < calStandard.getTime().getTime())
				checkedAccountList.add(account);
		}
		return checkedAccountList;
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
