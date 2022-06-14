package com.server.withme.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.model.VertexDto;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.ILocationService;
import com.server.withme.serivce.ISafeZoneService;
import com.server.withme.serivce.ITTLService;

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
	
	private final ITTLService ttlService;
	
	private final ISafeZoneService safeZoneService;
	
	private final IAccountOptionService accountOptionService;
		
    private final Logger logger = LoggerFactory.getLogger(LocationController.class);

	@PostMapping("/location/{accountId}")
    public ResponseEntity<SafeZoneInfoDto<VertexDto>> saveLocation (
    		@PathVariable UUID accountId,
            @Validated @RequestBody LocationDto locationDto
    ) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		VertexDto location = locationService.saveLocation(locationDto,accountOption);
			MDC.put("loggerFileName", accountId.toString()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			logger.info(accountOption.getAccount().getUsername() +"is creating location session start.");
			logger.info(accountId.toString()+","+location.getLatitude()+","+location.getLongitude()+"is saved location success.");
			MDC.remove("loggerFileName");
		SafeZoneInfoDto<VertexDto> safeZoneInfoDto = SafeZoneInfoDto
				.craeteSafeZoneInfoDto(new ArrayList<VertexDto>(Arrays.asList(location)),location.getTF(),0);
		return new ResponseEntity<>(safeZoneInfoDto,new HttpHeaders(),HttpStatus.OK);
	}
	
	
	@PostMapping("/location/in-out/{accountId}")
    public ResponseEntity<SafeZoneInfoDto<VertexDto>> checkLocationInAndOut (
    		@PathVariable UUID accountId,
            @Validated @RequestBody LocationDto locationDto
    ) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
		List<List<SafeZone>> totalSafeZoneList = new ArrayList<>();
		for(TTL ttl : ttlList) 
			totalSafeZoneList.add(safeZoneService.findByTTLIdOrThrow(ttl.getId()));
		VertexDto location = locationService.checkInAndOut(locationDto,accountOption,ttlList,totalSafeZoneList);
			MDC.put("loggerFileName", accountId.toString()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			logger.info(accountOption.getAccount().getUsername() +"is checking in/out location session start.");
			logger.info(accountId.toString()+"is checked location success.");
			logger.info((location.getTF()==true ? "person, out of safe zone." : "person, already inside of safe zone."));
			MDC.remove("loggerFileName");
		SafeZoneInfoDto<VertexDto> safeZoneInfoDto = SafeZoneInfoDto
				.craeteSafeZoneInfoDto(new ArrayList<VertexDto>(Arrays.asList(location)),location.getTF(),1);
		return new ResponseEntity<>(safeZoneInfoDto,new HttpHeaders(),HttpStatus.OK);
	}
}
