package com.server.withme.serivce;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.server.withme.entity.Location;
import com.server.withme.model.LocationDto;
/**
 * Interface for TTLService
 *
 * @author Jongseong Baek
 */
public interface ILocationService {

	public boolean checkLatestLocation(LocationDto locationDto , UUID accountId);
	
	public boolean saveLocation(LocationDto locationDto, UUID accountId);
		
	public Map<String,Boolean> checkInAndOut(LocationDto locationDto, UUID accountId);
	
	public List<Location> findByAccountOptionIdOrThrow(Integer accountOptionId);
}
