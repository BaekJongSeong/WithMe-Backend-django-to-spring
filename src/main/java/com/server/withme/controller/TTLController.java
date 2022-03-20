package com.server.withme.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.model.AccountIdDto;
import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.serivce.ITTLService;

import lombok.RequiredArgsConstructor;

/**
 * Controller for TTL API
 *
 * @author Jongseong Baek
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TTLController {

private final ITTLService ttlService;
	
	@PostMapping("/ttl")
    public ResponseEntity<SafeZoneInfoDto> saveTTL (
            @Validated @RequestBody AccountIdDto accountIdDto
    ) {
		ttlService.saveTTL(accountIdDto);
        return new ResponseEntity<>(SafeZoneInfoDto.builder()
        		.message("safe zone 등록에 성공하였습니다").build(),new HttpHeaders(),HttpStatus.OK);
    }
	
}
