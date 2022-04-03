package com.server.withme.util;

import java.util.List;
import java.util.Map;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.Location;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.LocationDto;
import com.server.withme.model.VertexDto;

public interface IVertexCheckUtil {

	public Map<String,String> checkSafeZoneMinSize(List<VertexDto> safeZoneList);
	
	public Map<String,Double> findMinMaxVertex(List<VertexDto> safeZoneList);
	
	public Integer countSafeZone(List<VertexDto> initSafeZoneListChanged);
	
	public int[] checkInAndOut(SafeZone target, List<SafeZone> totalSafeZoneList);
	
	public int findSafeZoneIndex(VertexDto vertexDto,Map<String,String> resultMap);
	
	public List<List<VertexDto>> checkInAndOutForUpdate(AccountOption accountOption,List<VertexDto> vertexDtoList,
			List<VertexDto> totalList,Map<String,String> resultMap);
	
	public List<SafeZone> checkInAndOutInitSafeSone(List<SafeZone> targetList, List<SafeZone> totalSafeZoneList);

	public List<SafeZone> checkInAndOutLocation(SafeZone target, List<List<SafeZone>> totalSafeZoneList, List<TTL> ttlList);

	public Boolean checkLatestLocation(LocationDto locationDto,Location location);
}
