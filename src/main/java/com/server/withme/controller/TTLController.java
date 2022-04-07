package com.server.withme.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.entity.AccountOption;
import com.server.withme.enumclass.DayCalculator;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.TTLDto;
import com.server.withme.model.VertexDto;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.ISafeZoneService;
import com.server.withme.serivce.ITTLService;
import com.server.withme.util.IVertexUtil;

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
	
	private final ISafeZoneService safeZoneService;
	
	private final IAccountOptionService accountOptionService;
	
	private final IVertexUtil vertexUtil;
	
	@PostMapping("/ttl")
    public ResponseEntity<TTLDto> saveTTL (
            @Validated @RequestBody AccountIdDto accountIdDto
    ) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountIdDto.getAccountId());
		
		List<VertexDto> vertexDtoList = vertexUtil.convertToVertexDto(
				safeZoneService.loadInitSafeZoneList(accountIdDto));
		
		ttlService.saveTTLFirstTime(vertexDtoList,accountOption);
		TTLDto ttlDto = ttlService.createTTLDto(DayCalculator.ZERO.calculateDay(new Timestamp(System.currentTimeMillis())).getTime());
        return new ResponseEntity<>(ttlDto,new HttpHeaders(),HttpStatus.OK);
    }
	
}
