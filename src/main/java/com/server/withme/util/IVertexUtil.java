package com.server.withme.util;

import java.util.List;
import java.util.Map;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.model.VertexDto;

public interface IVertexUtil {
	
	public Map<String,String> checkSafeZoneMinSize(List<VertexDto> initSafeZoneList);
	
	public Map<String,Double> findMinMaxVertex(List<VertexDto> initSafeZoneList);
	
	public Integer countSafeZone(AccountOption accountOption);
	
	public List<VertexDto> convertInitSafeZoneToVertexDto(List<InitSafeZone> initSafeZoneList);
	
	public List<VertexDto> calculateVertex(List<VertexDto> initSafeZoneList);
}
