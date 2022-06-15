package com.server.withme.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.entity.AccountOption;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneDto;
import com.server.withme.model.VertexDto;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.IMailService;
import com.server.withme.serivce.ISafeZoneService;
import com.server.withme.util.IVertexUtil;

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

	private final IAccountOptionService accountOptionService;
	
	private final ISafeZoneService safeZoneService;
	
	private final IMailService mailService;
	
	private final IVertexUtil vertexUtil;
	
    private final Logger logger = LoggerFactory.getLogger(SafeZoneController.class);

	@PostMapping("/safe-zone/init/{accountId}")
    public String saveInitSafeZone (
            @PathVariable UUID accountId,
            @Validated @RequestBody SafeZoneDto safeZoneDto,
            @RequestHeader("Authorization") String token
    ) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);			
		VertexDto vertexDto = safeZoneService.saveInitSafeZone(safeZoneDto, accountOption);
			MDC.put("loggerFileName", accountOption.getAccount().getAccountId()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			logger.info(accountOption.getAccount().getUsername() +" are going to input init-safe-zone.");
			logger.info(accountOption.getAccount().getAccountId() +"is saving init-safe-zone successed.");
			MDC.remove("loggerFileName");
			mailService.reDirectUrl(token.split(" ")[1],accountId.toString(),safeZoneDto);
		//SafeZoneInfoDto<VertexDto> safeZoneInfoDto = SafeZoneInfoDto.craeteSafeZoneInfoDto(
		//			new ArrayList<VertexDto>(Arrays.asList(vertexDto)), vertexDto.getTF(),0);
		return "http://121.154.58.201:8040/admin/?username=withmeuser&password=adminuser";
    }
	
	@PostMapping("/safe-zone/{accountId}")
    public ResponseEntity<SafeZoneDto> saveSafeZone (
            @PathVariable UUID accountId,
            @Validated @RequestBody SafeZoneDto safeZoneDto
    ) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);		
		List<VertexDto> safeZoneList= safeZoneService.saveSafeZoneFirstTime(vertexUtil.calculateVertex(safeZoneDto.getSafeZone()),accountOption);
			MDC.put("loggerFileName", accountId.toString()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			logger.info(accountOption.getAccount().getUsername() +"is saving all safe zone boxes refer to init-safe-zone");
			logger.info(safeZoneList.size() + " are created.\n" + safeZoneList.toString());
			MDC.remove("loggerFileName");
		return new ResponseEntity<>(SafeZoneDto.builder()
        		.safeZone(safeZoneList).build(),new HttpHeaders(),HttpStatus.OK);
    }
	
	@DeleteMapping("/safe-zone/first")
    public ResponseEntity<SafeZoneDto> deleteSafeZoneFirst (
    		@Validated @RequestBody AccountIdDto accountIdDto
    ) {		
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountIdDto.getAccountId());
		List<VertexDto> deleteSafeZoneList = safeZoneService.deleteSafeZoneFirstTime(accountOption);
			MDC.put("loggerFileName", accountIdDto.getAccountId().toString()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			logger.info(accountOption.getAccount().getUsername() +"is removing safe zone boxes");
			logger.info(deleteSafeZoneList.size() + " are removed.");
			MDC.remove("loggerFileName");
        return new ResponseEntity<>(SafeZoneDto.builder()
        		.safeZone(deleteSafeZoneList).build(),new HttpHeaders(),HttpStatus.OK);
    }
	
	@PostMapping("/safe-zone/location/{accountId}")
    public ResponseEntity<SafeZoneDto> createSafeZoneByLocation (
            @PathVariable UUID accountId,
            @Validated @RequestBody LocationDto locationDto
    ) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		List<VertexDto> safeZone = safeZoneService.createSafeZoneByLocation(accountOption,locationDto);
			MDC.put("loggerFileName", accountId.toString()+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			logger.info(accountOption.getAccount().getUsername() +"is creating addtional safe zone boxes");
			logger.info(safeZone.toString());
			MDC.remove("loggerFileName");
		return new ResponseEntity<>(SafeZoneDto.builder()
        		.safeZone(safeZone).build(),new HttpHeaders(),HttpStatus.OK);
    }
	
	
}
