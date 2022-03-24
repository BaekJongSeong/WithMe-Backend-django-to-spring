package com.server.withme.controller;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.model.AccountIdDto;
import com.server.withme.model.InitSafeZoneDto;
import com.server.withme.model.SafeZoneInfoDto;
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
    public ResponseEntity<SafeZoneInfoDto> saveInitSafeZone (
            @PathVariable UUID accountId,
            @Validated @RequestBody InitSafeZoneDto initSafeZoneDto
    ) {
		SafeZoneInfoDto safeZoneInfoDto = new SafeZoneInfoDto();
		if(safeZoneService.saveInitSafeZone(initSafeZoneDto, accountId))
			safeZoneInfoDto.setMessage("safe zone 등록이 완료되었습니다.");
		else
			safeZoneInfoDto.setMessage("safe zone 최소 size를 위반하여 등록되지 않았습니다.");
			return new ResponseEntity<>(safeZoneInfoDto,new HttpHeaders(),HttpStatus.OK);
    }
	
	@PostMapping("/safe-zone/{accountId}")
    public ResponseEntity<SafeZoneInfoDto> saveSafeZone (
            @PathVariable UUID accountId,
            @Validated @RequestBody InitSafeZoneDto initSafeZoneDto
    ) {
		safeZoneService.saveSafeZone(initSafeZoneDto,accountId);
        return new ResponseEntity<>(SafeZoneInfoDto.builder()
        		.message("safe zone을 분할하여 전체 등록에 성공하였습니다").build(),new HttpHeaders(),HttpStatus.OK);
    }
	
	@DeleteMapping("/safe-zone-first")
    public ResponseEntity<SafeZoneInfoDto> deleteSafeZoneFirst (
    		@Validated @RequestBody AccountIdDto accountIdDto
    ) {
		safeZoneService.deleteSafeZoneFirst(accountIdDto.getAccountId());
        return new ResponseEntity<>(SafeZoneInfoDto.builder()
        		.message("safe zone을 유저 맞춤형으로 정교화하였습니다").build(),new HttpHeaders(),HttpStatus.OK);
    }
	
}
