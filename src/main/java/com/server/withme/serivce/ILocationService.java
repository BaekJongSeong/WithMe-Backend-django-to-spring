package com.server.withme.serivce;

import java.util.List;
import java.util.UUID;

import com.server.withme.entity.Location;
import com.server.withme.model.LocationDto;
import com.server.withme.model.VertexDto;
/**
 * Interface for TTLService
 *
 * @author Jongseong Baek
 */
public interface ILocationService {

	public boolean checkLatestLocation(LocationDto locationDto , UUID accountId);
	
	public List<VertexDto> saveLocation(LocationDto locationDto, UUID accountId);
		
	public List<VertexDto> checkInAndOut(LocationDto locationDto, UUID accountId);
	
	public VertexDto createVertexDto(double latitude, double longitude);
	
	public List<Location> findByAccountOptionIdOrThrow(Integer accountOptionId);
}
