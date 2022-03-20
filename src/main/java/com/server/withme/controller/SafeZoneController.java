package com.server.withme.controller;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.model.InitSafeZoneDto;
import com.server.withme.serivce.ISafeZoneService;

import lombok.RequiredArgsConstructor;

/**
 * Controller for SafeZone API
 *
 * @author Jongseong Baek
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SafeZoneController {

	private final ISafeZoneService safeZoneService;
	
	@PostMapping("/init-safe-zone/{accountId}")
    public ResponseEntity<String> saveInitSafeZone (
            @PathVariable UUID accountId,
            @Validated @RequestBody InitSafeZoneDto initSafeZoneDto
    ) {
		safeZoneService.saveInitSafeZone(initSafeZoneDto, accountId);
        return new ResponseEntity<>("safe zone 등록에 성공하였습니다",new HttpHeaders(),HttpStatus.OK);
    }
	
	@PostMapping("/zone-location/{accountId}")
    public ResponseEntity<String> saveZoneLocation (
            @PathVariable UUID accountId,
            @Validated @RequestBody InitSafeZoneDto initSafeZoneDto
    ) {
		safeZoneService.saveSafeZone(initSafeZoneDto,accountId);
        return new ResponseEntity<>("safe zone을 분할하여 전체 등록에 성공하였습니다",new HttpHeaders(),HttpStatus.OK);
    }
	
}
