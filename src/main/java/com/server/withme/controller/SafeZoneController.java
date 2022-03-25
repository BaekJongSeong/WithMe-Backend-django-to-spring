package com.server.withme.controller;

import java.util.ArrayList;
import java.util.List;
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

import com.server.withme.entity.SafeZone;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneDto;
import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.model.VertexDto;
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
	
	@PostMapping("/safe-zone/init/{accountId}")
    public ResponseEntity<SafeZoneInfoDto<VertexDto>> saveInitSafeZone (
            @PathVariable UUID accountId,
            @Validated @RequestBody SafeZoneDto safeZoneDto
    ) {
		List<VertexDto> safeZoneList = safeZoneService.saveInitSafeZone(safeZoneDto, accountId);
		SafeZoneInfoDto<VertexDto> safeZoneInfoDto = safeZoneService
				.craeteSafeZoneInfoDto(safeZoneList, safeZoneList.remove(safeZoneList.size()-1).getLatitude());
		return new ResponseEntity<>(safeZoneInfoDto,new HttpHeaders(),HttpStatus.OK);
    }
	
	@PostMapping("/safe-zone/{accountId}")
    public ResponseEntity<SafeZoneDto> saveSafeZone (
            @PathVariable UUID accountId,
            @Validated @RequestBody SafeZoneDto safeZoneDto
    ) {
		List<VertexDto> safeZoneList= safeZoneService.saveSafeZoneFirstTime(safeZoneDto,accountId);
        return new ResponseEntity<>(SafeZoneDto.builder()
        		.safeZone(safeZoneList).build(),new HttpHeaders(),HttpStatus.OK);
    }
	
	@DeleteMapping("/safe-zone/first")
    public ResponseEntity<SafeZoneDto> deleteSafeZoneFirst (
    		@Validated @RequestBody AccountIdDto accountIdDto
    ) {
		List<VertexDto> deleteSafeZoneList = safeZoneService.deleteSafeZoneFirstTime(accountIdDto.getAccountId());
        return new ResponseEntity<>(SafeZoneDto.builder()
        		.safeZone(deleteSafeZoneList).build(),new HttpHeaders(),HttpStatus.OK);
    }
	
	@PostMapping("/safe-zone/location/{accountId}")
    public ResponseEntity<SafeZoneDto> createSafeZoneByLocation (
            @PathVariable UUID accountId,
            @Validated @RequestBody LocationDto locationDto
    ) {
		List<VertexDto> safeZone = safeZoneService.createSafeZoneByLocation(accountId,locationDto);
		return new ResponseEntity<>(SafeZoneDto.builder()
        		.safeZone(safeZone).build(),new HttpHeaders(),HttpStatus.OK);
    }
	
	
}
