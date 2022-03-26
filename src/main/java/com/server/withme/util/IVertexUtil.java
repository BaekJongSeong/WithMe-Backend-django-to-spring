package com.server.withme.util;

import java.util.List;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.Location;
import com.server.withme.entity.SafeZone;
import com.server.withme.model.LocationDto;
import com.server.withme.model.VertexDto;

public interface IVertexUtil {
	
	public List<VertexDto> convertLocationToVertexDto(List<Location> locationList);
	
	public List<VertexDto> convertInitSafeZoneToVertexDto(List<InitSafeZone> initSafeZoneList);
	
	public List<SafeZone> convertInitSafeZoneToSafeZone(List<InitSafeZone> initSafeZoneList);
	
	public List<VertexDto> convertSafeZoneToVertexDto(List<SafeZone> initSafeZoneList);
	
	public List<VertexDto> createNewSafeZone(List<VertexDto> safeZoneList, double x, double y, double perBoxSize);
	
	public List<VertexDto> calculateVertex(List<VertexDto> initSafeZoneList);
	
	public List<VertexDto> createSafeZoneByLocation(AccountOption accountOption,LocationDto locationDto);
	
	public List<SafeZone> calculateDeleteVertex(List<SafeZone> safeZoneList,AccountOption accountOption);
}
