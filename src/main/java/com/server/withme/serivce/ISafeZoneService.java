package com.server.withme.serivce;

import java.util.List;
import java.util.UUID;

import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.SafeZone;
import com.server.withme.model.InitSafeZoneDto;

/**
 * Interface for SafeZoneService
 *
 * @author Jongseong Baek
 */
public interface ISafeZoneService {

	public Boolean saveInitSafeZone(InitSafeZoneDto initSafeZoneDto, UUID accountId);
	
	public void saveSafeZone(InitSafeZoneDto initSafeZoneDto, UUID accountId);
	
	public void deleteSafeZoneFirst(UUID accountId);
	
	public List<InitSafeZone> findByAccountOptionIdOrThrow(Integer accountOptionId);
	
	public List<SafeZone> findByTTLIdOrThrow(Integer ttlId);
	
}
