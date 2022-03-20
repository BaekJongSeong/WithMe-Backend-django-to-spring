package com.server.withme.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.model.SendMailDto;
import com.server.withme.serivce.IMailService;

import lombok.RequiredArgsConstructor;

/**
 * Controller for SendMail API
 *
 * @author Jongseong Baek
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MailController {
	
	private final IMailService mailService;
	
	@PostMapping("/mail")
    public ResponseEntity<SafeZoneInfoDto> sendMail (
            @Validated @RequestBody SendMailDto sendMailDto
    ) {
		mailService.sendMail(sendMailDto);
		
		 return new ResponseEntity<>(SafeZoneInfoDto.builder()
	        		.message("이메일 전송에 성공하였습니다.").build(),new HttpHeaders(),HttpStatus.OK);
    }
}
