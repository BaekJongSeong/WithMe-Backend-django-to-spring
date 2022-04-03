package com.server.withme.controller;

import java.util.List;
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

import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.model.VertexDto;
import com.server.withme.serivce.ILocationService;
import com.server.withme.serivce.ISafeZoneService;

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
	
	private final ISafeZoneService safeZoneService;
	
	@PostMapping("/location/{accountId}")
    public ResponseEntity<SafeZoneInfoDto<VertexDto>> saveLocation (
    		@PathVariable UUID accountId,
            @Validated @RequestBody LocationDto locationDto
    ) {
		List<VertexDto> location = locationService.saveLocation(locationDto,accountId);
		SafeZoneInfoDto<VertexDto> safeZoneInfoDto = safeZoneService
				.craeteSafeZoneInfoDto(location,location.remove(location.size()-1).getLatitude(),0);
		return new ResponseEntity<>(safeZoneInfoDto,new HttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/location/in-out/{accountId}")
    public ResponseEntity<SafeZoneInfoDto<VertexDto>> checkLocationInAndOut (
    		@PathVariable UUID accountId,
            @Validated @RequestBody LocationDto locationDto
    ) {
		List<VertexDto> location = locationService.checkInAndOut(locationDto,accountId);
		SafeZoneInfoDto<VertexDto> safeZoneInfoDto = safeZoneService
				.craeteSafeZoneInfoDto(location,location.remove(location.size()-1).getLatitude(),1);
		return new ResponseEntity<>(safeZoneInfoDto,new HttpHeaders(),HttpStatus.OK);
	}
}
