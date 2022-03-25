package com.server.withme.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.entity.TTL;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.TTLDto;
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
    public ResponseEntity<TTLDto> saveTTL (
            @Validated @RequestBody AccountIdDto accountIdDto
    ) {
		TTL ttl = ttlService.saveTTLFirstTime(accountIdDto);
		TTLDto ttlDto = ttlService.createTTLDto(ttl);
        return new ResponseEntity<>(ttlDto,new HttpHeaders(),HttpStatus.OK);
    }
	
}
