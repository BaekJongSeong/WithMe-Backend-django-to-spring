package com.server.withme.serivce;

import java.util.Map;
import java.util.UUID;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.Location;
import com.server.withme.model.LocationDto;
/**
 * Interface for TTLService
 *
 * @author Jongseong Baek
 */
public interface ILocationService {

	public Map<String,Boolean> saveLocation(LocationDto locationDto, UUID accountId);
	
	public boolean checkInAndOut(LocationDto locationDto,AccountOption accountOption);
}
