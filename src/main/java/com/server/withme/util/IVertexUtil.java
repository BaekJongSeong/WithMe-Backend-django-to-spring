package com.server.withme.util;

import java.util.List;
import java.util.Map;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.Location;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.LocationDto;
import com.server.withme.model.VertexDto;

public interface IVertexUtil {
	
	public Map<String,String> checkSafeZoneMinSize(List<VertexDto> initSafeZoneList);
	
	public Map<String,Double> findMinMaxVertex(List<VertexDto> initSafeZoneList);
	
	public Integer countSafeZone(AccountOption accountOption);
	
	public List<VertexDto> convertInitSafeZoneToVertexDto(List<InitSafeZone> initSafeZoneList);
	
	public List<SafeZone> convertInitSafeZoneToSafeZone(List<InitSafeZone> initSafeZoneList);
	
	public List<VertexDto> convertSafeZoneToVertexDto(List<SafeZone> initSafeZoneList);
	
	public List<VertexDto> calculateVertex(List<VertexDto> initSafeZoneList);
	
	public List<SafeZone> calculateDeleteVertex(List<SafeZone> safeZoneList,AccountOption accountOption);
	
	public int[] checkInAndOut(SafeZone target, List<SafeZone> totalSafeZoneList);
	
	public List<SafeZone> checkInAndOutInitSafeSone(List<SafeZone> targetList, List<SafeZone> totalSafeZoneList);

	public List<SafeZone> checkInAndOutLocation(SafeZone target, List<List<SafeZone>> totalSafeZoneList, List<TTL> ttlList);

	public Boolean checkLatestLocation(LocationDto locationDto,Location location);
	
	public List<SafeZone> createSafeZoneByLocation(AccountOption accountOption);
}
