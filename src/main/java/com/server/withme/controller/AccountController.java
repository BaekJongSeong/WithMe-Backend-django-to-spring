package com.server.withme.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.configure.JwtFilter;
import com.server.withme.configure.TokenProvider;
import com.server.withme.model.AccountDto;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.LoginDto;
import com.server.withme.model.SignupDto;
import com.server.withme.model.TokenDto;
import com.server.withme.serivce.IAccountService;

import lombok.RequiredArgsConstructor;

/**
 * Controller for Authentication API
 *
 * @author Jongseong Baek
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final IAccountService accountService;
    
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    
    private final TokenProvider tokenProvider;

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @PostMapping("/account")
    public ResponseEntity<AccountDto> signup (
            @Validated @RequestBody SignupDto signupDto
    ) {
    	AccountDto accountDto =AccountDto.createAccountDto(accountService.checkForSignup(signupDto));
    		MDC.put("loggerFileName", accountDto.getAccountId().toString()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    		logger.info(signupDto.getLoginDto().getUsername() +"is creating account session start.");
    		logger.info(accountDto.getAccountId().toString() +"is created success");
    		MDC.remove("loggerFileName");
    	return new ResponseEntity<>(accountDto,new HttpHeaders(),HttpStatus.OK);
    }

    @PutMapping("/account")
    public ResponseEntity<AccountDto> modifyAccount (
            @Validated @RequestBody AccountIdDto accountIdDto
    ) {
    	AccountDto accountDto =AccountDto.createAccountDto(accountService.modifyAccount(accountIdDto));
    		MDC.put("loggerFileName", accountDto.getAccountId().toString()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    		logger.info(accountDto.getUsername() +"is modifying account session start.");
    		logger.info(accountDto.getAccountId().toString() +"is modified success.");
    		MDC.remove("loggerFileName");
        return new ResponseEntity<>(accountDto,new HttpHeaders(),HttpStatus.OK);
    }
    
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login (
            @Validated @RequestBody LoginDto loginDto
    ) {
    	//가독성을 위해서 이름이 긴 클래스는 이렇게 엔터 쳐서 들어가기
    	UserDetails user = accountService.loadUserByUsername(loginDto.getUsername());
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                		user, loginDto.getPassword()
                );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        AccountIdDto accountIdDto = accountService.getAccountId(loginDto);
	        MDC.put("loggerFileName", accountIdDto.getAccountId().toString()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			logger.info(loginDto.getUsername() +"is login session start.");
			logger.info(accountIdDto.getAccountId().toString() +"is logined success.");
			MDC.remove("loggerFileName");
        
        return new ResponseEntity<TokenDto>(TokenDto.builder().accountId(accountIdDto.getAccountId())
        									.token(jwt).build(), httpHeaders, HttpStatus.OK);
    }
}