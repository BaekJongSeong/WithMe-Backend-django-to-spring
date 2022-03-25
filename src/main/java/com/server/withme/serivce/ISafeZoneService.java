package com.server.withme.serivce;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.SafeZone;
import com.server.withme.model.SafeZoneDto;
import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.model.VertexDto;
import com.server.withme.model.LocationDto;

/**
 * Interface for SafeZoneService
 *
 * @author Jongseong Baek
 */
public interface ISafeZoneService {

	public List<VertexDto> saveInitSafeZone(SafeZoneDto initSafeZoneDto, UUID accountId);
	
	public List<VertexDto> saveSafeZoneFirstTime(SafeZoneDto initSafeZoneDto, UUID accountId);
	
	public List<VertexDto> deleteSafeZoneFirstTime(UUID accountId);
	
	public List<VertexDto> createSafeZoneByLocation (UUID accountId,LocationDto locationDto);
	
	public <T> SafeZoneInfoDto<T> craeteSafeZoneInfoDto(List<T> list,double trueOrFalse);
	
	public List<InitSafeZone> findByAccountOptionIdOrThrow(Integer accountOptionId);
	
	public List<SafeZone> findByTTLIdOrThrow(Integer ttlId);
}
