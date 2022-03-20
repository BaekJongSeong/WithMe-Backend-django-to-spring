package com.server.withme.serivce;

import java.util.UUID;

import com.server.withme.model.InitSafeZoneDto;

/**
 * Interface for SafeZoneService
 *
 * @author Jongseong Baek
 */
public interface ISafeZoneService {

	public void saveInitSafeZone(InitSafeZoneDto initSafeZoneDto, UUID accountId);
	
	public void saveSafeZone(InitSafeZoneDto initSafeZoneDto, UUID accountId);
	
}
