package com.server.withme.serivce;

import java.util.List;
import java.util.UUID;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.Location;
import com.server.withme.model.LocationDto;
import com.server.withme.model.VertexDto;
/**
 * Interface for TTLService
 *
 * @author Jongseong Baek
 */
public interface ILocationService {

	public boolean checkLatestLocation(LocationDto locationDto , AccountOption accountOption);
	
	public VertexDto saveLocation(LocationDto locationDto, AccountOption accountOption);
		
	public VertexDto checkInAndOut(LocationDto locationDto, AccountOption accountOption);
	
	public List<Location> findByAccountOptionIdOrThrow(Integer accountOptionId);
}
