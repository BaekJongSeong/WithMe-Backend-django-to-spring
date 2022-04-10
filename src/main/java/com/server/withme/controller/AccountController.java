package com.server.withme.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.configure.JwtFilter;
import com.server.withme.configure.TokenProvider;
import com.server.withme.entity.Account;
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

    @PostMapping("/sign")
    public ResponseEntity<Account> signup (
            @Validated @RequestBody SignupDto signupDto
    ) {
        return ResponseEntity.ok(accountService.checkForSignup(signupDto));
    }

    @PutMapping("/account")
    public ResponseEntity<AccountDto> modifyAccount (
            @Validated @RequestBody AccountIdDto accountIdDto
    ) {
    	Account account = accountService.modifyAccount(accountIdDto);
    	AccountDto accountDto =AccountDto.createAccountDto(account);
        return new ResponseEntity<>(accountDto,new HttpHeaders(),HttpStatus.OK);
    }
    
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login (
            @Validated @RequestBody LoginDto loginDto
    ) {
    	//가독성을 위해서 이름이 긴 클래스는 이렇게 엔터 쳐서 들어가기
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(), loginDto.getPassword()
                );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        AccountIdDto accountIdDto = accountService.getAccountId(loginDto);
        
        return new ResponseEntity<TokenDto>(TokenDto.builder().accountId(accountIdDto.getAccountId())
        									.token(jwt).build(), httpHeaders, HttpStatus.OK);
    }
}