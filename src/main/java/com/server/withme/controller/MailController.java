package com.server.withme.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.withme.FirebaseCloudMessageService;
import com.server.withme.entity.Account;
import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.model.SendMailDto;
import com.server.withme.serivce.IAccountService;
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
	
	private final IAccountService accountService;
	
    private final Logger logger = LoggerFactory.getLogger(MailController.class);
    
	private final FirebaseCloudMessageService firebaseService;
	
	@Value("${API.token}")
	private String token;

	@PostMapping("/mail")
    public ResponseEntity<SafeZoneInfoDto<String>> sendMail (
            @Validated @RequestBody SendMailDto sendMailDto
    ) {
		mailService.sendMail(sendMailDto);
		Account account = accountService.findByEmail(sendMailDto.getEmail());
		SafeZoneInfoDto<String> safeZoneInfoDto = new SafeZoneInfoDto<>();
			MDC.put("loggerFileName", account.getAccountId()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			logger.info(account.getUsername() +" are waiting for mail send session.");
			logger.info("mail is send to "+account.getAccountId() +" successed.");
			MDC.remove("loggerFileName");
		safeZoneInfoDto.setMessage("이메일 전송에 성공하였습니다.");
		
		 return new ResponseEntity<>(safeZoneInfoDto,new HttpHeaders(),HttpStatus.OK);
    }
	
	@PostMapping("/alarm/{username}/{name}/{latitude}/{longitude}")
	public ResponseEntity getAlarmMessage(@RequestBody LocationDto locationDto, @PathVariable String username,
			@PathVariable String name, @PathVariable Double latitude, @PathVariable Double longitude) throws IOException, FirebaseMessagingException{
		Account account = accountService.findByUsernameOrThrow(username);
		String content = mailService.pathSearching(name,latitude,longitude,locationDto);
		firebaseService.sendMessageTo(token,mailService.makeAlarmTitle(username),content);
			MDC.put("loggerFileName", account.getAccountId()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			logger.info(account.getUsername() +" are waiting for alarm send session.");
			logger.info("alarm is send to "+account.getAccountId() +" successed.\n" + content);
			MDC.remove("loggerFileName");
		return ResponseEntity.ok().build();
	}
}
