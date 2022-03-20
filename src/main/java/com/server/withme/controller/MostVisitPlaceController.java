package com.server.withme.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.server.withme.entity.Account;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.SendMailDto;
import com.server.withme.serivce.IAccountService;
import com.server.withme.serivce.IMailService;

import lombok.RequiredArgsConstructor;

/**
 * API Controller
 *
 * @author Jongseong Baek
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/best-visit-place")
public class MostVisitPlaceController {

	private final IMostVisitPlaceService mostVisitPlaceService;
	private final IAccountService accountService;
	private final IMailService mailService;
	private final ILocationService locationService;
	
	//초기화 방법이 두개. dto = method();해서 반환값 받는거랑 + dto.Builder().id().build();해서 setter로 들어가는거랑
	//Service 클래스들의 메소드들 명도 뭔가 규칙이 있는거같음.get뭐by뭐orthrow 또는 그냥 get뭐by뭐
	
	@GetMapping("/{accountId}")
    public ResponseEntity<MostVisitPlaceListDto> mostVisitPlace(
    		@PathVariable UUID accountId
    		) {
		Account account = accountService.getAccountByAccountIdOrThrow(accountId);
		
		AccountIdDto accountIdDto= AccountIdDto.builder()
				.accountId(account.getAccountId()).build();
		
		MostVisitPlaceListDto mostVisitPlaceListDto = MostVisitPlaceListDto.builder()
				.elements(mostVisitPlaceService.detectMostVisitPlaceByUserId(accountIdDto)).build();
		
        return ResponseEntity.ok(mostVisitPlaceListDto);
    }
	
	
	@GetMapping("/send-location-result/{accountId}")
	public boolean sendPlaceLocationToMail(
			@PathVariable UUID accountId,
			@Validated @RequestBody SendMailDto mailDto
			) {
		Account account = accountService.getAccountByAccountIdOrThrow(accountId);
		
		AccountIdDto accountIdDto= AccountIdDto.builder()
				.accountId(account.getAccountId()).build();
		
		MostVisitPlaceListDto mostVisitPlaceListDto = MostVisitPlaceListDto.builder()
				.elements(mostVisitPlaceService.detectMostVisitPlaceByUserId(accountIdDto)).build();
		
		LocationListDto locationListDto = LocationListDto.builder()
				.elements(locationService.detectLocationByPlace(mostVisitPlaceListDto)).build();
		
		Context ctx = mostVisitPlaceService.getEmpathyResultMailContents(locationListDto, account);
		String contents = mailService.processContextToString(ctx);

		String title = "[WithMe] 자주 방문한 장소의 좌표입니다.";
		mailService.sendMail(mailDto.getEmail(), title, contents);

		return true;
	}
}
