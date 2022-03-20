package com.server.withme.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.model.AccountIdDto;
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
	
	@PostMapping("/safe-zone/{count}")
    public ResponseEntity<String> saveTTL (
            @PathVariable int count,
            @Validated @RequestBody AccountIdDto accountIdDto
    ) {
		ttlService.saveTTL(count, accountIdDto);
        return new ResponseEntity<>("safe zone 등록에 성공하였습니다",new HttpHeaders(),HttpStatus.OK);
    }
	
}
