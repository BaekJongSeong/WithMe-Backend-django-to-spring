package com.server.withme.serivce;

import java.util.List;
import java.util.UUID;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.SafeZone;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneDto;
import com.server.withme.model.VertexDto;

/**
 * Interface for SafeZoneService
 *
 * @author Jongseong Baek
 */
public interface ISafeZoneService {

	public VertexDto saveInitSafeZone(SafeZoneDto initSafeZoneDto, AccountOption accountOption);
	
	public void saveAll(List<SafeZone> safeZoneList, int count);
	
	public List<VertexDto> saveSafeZoneFirstTime(List<VertexDto> safeZone, AccountOption accountOption);
	
	public List<VertexDto> deleteSafeZoneFirstTime(AccountOption accountOption);
	
	public List<VertexDto> createSafeZoneByLocation (AccountOption accountOption,LocationDto locationDto);
			
	public List<InitSafeZone> loadInitSafeZoneList(AccountIdDto accountIdDto);
		
	public List<InitSafeZone> findByAccountOptionIdOrThrow(Integer accountOptionId);
	
	public List<SafeZone> findByTTLIdOrThrow(Integer ttlId);
}
