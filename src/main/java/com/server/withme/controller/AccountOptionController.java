package com.server.withme.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.entity.Account;
import com.server.withme.entity.AccountOption;
import com.server.withme.model.AccountOptionDto;
import com.server.withme.model.SignupDto;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.IAccountService;

import lombok.RequiredArgsConstructor;
/**
 * Controller for AccountOption API
 *
 * @author Jongseong Baek
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountOptionController {

	private final IAccountService accountService;
	
	private final IAccountOptionService accountOptionService;
	
    private final Logger logger = LoggerFactory.getLogger(AccountOptionController.class);
	
	 @PostMapping("/account-option")
	    public ResponseEntity<AccountOptionDto> signupOption (
	            @Validated @RequestBody SignupDto signupDto
	    ) {
			Account account = accountService.findByUsernameOrThrow(signupDto.getLoginDto().getUsername());
		 	AccountOption accountOption = accountOptionService.signUpOption(account);
		 	AccountOptionDto accountOptionDto = AccountOptionDto.createAccountOptionDto(accountOption);
			 	MDC.put("loggerFileName", account.getAccountId().toString()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				logger.info(account.getUsername() +"is creating option session start.");
				logger.info(account.getAccountId().toString() +"is created option success.");
				MDC.remove("loggerFileName");
	        return new ResponseEntity<>(accountOptionDto,new HttpHeaders(), HttpStatus.OK);
	    }
}
