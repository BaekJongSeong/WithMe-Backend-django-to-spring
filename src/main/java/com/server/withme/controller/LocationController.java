package com.server.withme.controller;

import java.util.Map;
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

import com.server.withme.entity.SafeZone;
import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.serivce.ILocationService;

import lombok.RequiredArgsConstructor;

/**
 * Controller for Location API
 *
 * @author Jongseong Baek
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LocationController {
	
	private final ILocationService locationService;
	
	@PostMapping("/location/{accountId}")
    public ResponseEntity<SafeZoneInfoDto> saveLocation (
    		@PathVariable UUID accountId,
            @Validated @RequestBody LocationDto locationDto
    ) {
		SafeZoneInfoDto safeZoneInfoDto = new SafeZoneInfoDto();
		if(locationService.saveLocation(locationDto,accountId))
			safeZoneInfoDto.setMessage("location이 저장되었습니다.");
		else
			safeZoneInfoDto.setMessage("현재 location은 중복으로 변동이 없어 저장되지 못하였습니다.");
		return new ResponseEntity<>(safeZoneInfoDto,new HttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/location/in-out/{accountId}")
    public ResponseEntity<SafeZoneInfoDto> checkLocationInAndOut (
    		@PathVariable UUID accountId,
            @Validated @RequestBody LocationDto locationDto
    ) {
		SafeZoneInfoDto safeZoneInfoDto = new SafeZoneInfoDto();
		Map<String,Boolean> map = locationService.checkInAndOut(locationDto,accountId);
			if(map.get("inAndOut"))
				safeZoneInfoDto.setMessage("location에 해당하는 safeZone의 TTL이 업데이트 되었습니다.");
			else
				safeZoneInfoDto.setMessage("새로운 safeZone이 생성되었습니다.");
		return new ResponseEntity<>(safeZoneInfoDto,new HttpHeaders(),HttpStatus.OK);
	}
}
