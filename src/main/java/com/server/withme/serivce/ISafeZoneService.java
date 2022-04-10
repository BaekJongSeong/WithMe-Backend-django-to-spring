package com.server.withme.serivce;

import java.util.List;
import java.util.UUID;

import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.SafeZone;
import com.server.withme.model.SafeZoneDto;
import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.model.VertexDto;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.LocationDto;

/**
 * Interface for SafeZoneService
 *
 * @author Jongseong Baek
 */
public interface ISafeZoneService {

	public VertexDto saveInitSafeZone(SafeZoneDto initSafeZoneDto, UUID accountId);
	
	public List<VertexDto> saveSafeZoneFirstTime(List<VertexDto> safeZone, UUID accountId);
	
	public List<VertexDto> deleteSafeZoneFirstTime(UUID accountId);
	
	public List<VertexDto> createSafeZoneByLocation (UUID accountId,LocationDto locationDto);
			
	public List<InitSafeZone> loadInitSafeZoneList(AccountIdDto accountIdDto);
		
	public List<InitSafeZone> findByAccountOptionIdOrThrow(Integer accountOptionId);
	
	public List<SafeZone> findByTTLIdOrThrow(Integer ttlId);
}
