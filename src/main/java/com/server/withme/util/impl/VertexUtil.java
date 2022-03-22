package com.server.withme.util.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.SafeZone;
import com.server.withme.model.VertexDto;
import com.server.withme.repository.InitSafeZoneRepository;
import com.server.withme.util.IVertexUtil;

import lombok.RequiredArgsConstructor;
/**
 * Util for Vertex(check in and out, check min / max vertex)
 *
 * @author Jongseong Baek
 */
@RequiredArgsConstructor
@Component
public class VertexUtil implements IVertexUtil{
	
	private final InitSafeZoneRepository initSafeZoneRepository;
	
	//4.23KM 기준으로 움직임
	@Override
	public Map<String,String> checkSafeZoneMinSize(List<VertexDto> initSafeZoneList) {
		
		int state = 0;
		Map<String,String> result = new HashMap<>();
		Map<String,Double> minMaxMap = findMinMaxVertex(initSafeZoneList);
		result.put("maxLatitude", String.valueOf(minMaxMap.get("maxLatitude")));
		result.put("minLongitude", String.valueOf(minMaxMap.get("minLongitude")));
		
	    if (minMaxMap.get("maxLatitude") - minMaxMap.get("minLatitude") > 0.0423) {
	        if (minMaxMap.get("maxLongitude") - minMaxMap.get("minLongitude") > 0.0423) result.put("result", "true");
	        else result.put("result", "false");
	    }
	    else result.put("result", "false");
		return result;
	}
	
	@Override
	public Map<String,Double> findMinMaxVertex(List<VertexDto> initSafeZoneList){
		
		Map<String,Double> minMaxMap = new HashMap<>();
		
		initSafeZoneList.sort(Comparator.comparing(VertexDto::getLatitude).reversed());
		minMaxMap.put("maxLatitude",initSafeZoneList.get(0).getLatitude());
		minMaxMap.put("minLatitude",initSafeZoneList.get(initSafeZoneList.size()-1).getLatitude());
		
		initSafeZoneList.sort(Comparator.comparing(VertexDto::getLongitude).reversed());
		minMaxMap.put("maxLongitude",initSafeZoneList.get(0).getLongitude());
		minMaxMap.put("minLongitude",initSafeZoneList.get(initSafeZoneList.size()-1).getLongitude());
		return minMaxMap;
	}
	
	@Override
	public Integer countSafeZone(AccountOption accountOption) {
		
		List<InitSafeZone> initSafeZoneList= initSafeZoneRepository.findByJoinFetch(accountOption.getId());
		List<VertexDto> initSafeZoneListChanged = this.convertInitSafeZoneToVertexDto(initSafeZoneList);
		
		Map<String,Double> minMaxMap = this.findMinMaxVertex(initSafeZoneListChanged);
		
		Double x= minMaxMap.get("maxLatitude");
		Double perBoxSize = (double) (100/100000);
		Integer row=0,col=0;
		
		while(x > minMaxMap.get("minLatitude")) {
			
			Double y=minMaxMap.get("minLongitude");
			
			while(y < minMaxMap.get("maxLongitude")) {
				y += perBoxSize;
				col++;
			}
			x += perBoxSize;
			row++;
		}
		return row * col;
	}
	
	@Override
	public List<VertexDto> convertInitSafeZoneToVertexDto(List<InitSafeZone> initSafeZoneList){
		
		List<VertexDto> initSafeZoneListChanged = new ArrayList<>();
		
		for(InitSafeZone initSafeZone : initSafeZoneList) {
			
			VertexDto vertexDto = VertexDto.builder()
				.latitude(initSafeZone.getLatitude())
				.longitude(initSafeZone.getLongitude()).build();
		
			initSafeZoneListChanged.add(vertexDto);
		}
		
		return initSafeZoneListChanged;
	}
	
	@Override
	public List<VertexDto> calculateVertex(List<VertexDto> initSafeZoneList){
		
		List<VertexDto> safeZoneList = new ArrayList<>();
		Map<String,Double> minMaxMap = this.findMinMaxVertex(initSafeZoneList);
		
		Double x= minMaxMap.get("maxLatitude");
		Double perBoxSize = (double) (100/100000);
		
		while(x > minMaxMap.get("minLatitude")) {
			
			Double y=minMaxMap.get("minLongitude");
			
			while(y < minMaxMap.get("maxLongitude")) {
				safeZoneList.add(new VertexDto(x , y));
				safeZoneList.add(new VertexDto(x , y + perBoxSize));
				safeZoneList.add(new VertexDto(x - perBoxSize , y + perBoxSize));
				safeZoneList.add(new VertexDto(x - perBoxSize , y));
				y += perBoxSize;
			}
			x += perBoxSize;
		}
		return safeZoneList;
	}
	
	@Override
	public List<SafeZone> calculateDeleteVertex(List<SafeZone> safeZoneList){
		List<SafeZone> deleteSafeZoneList = new ArrayList<>();
		
		for(int idx=0; idx<safeZoneList.size(); idx+=4)
			deleteSafeZoneList.addAll(this.vertifyInAndOut(safeZoneList.subList(idx, idx+4)));

		return deleteSafeZoneList;
	}
	
	@Override
	public List<SafeZone> vertifyInAndOut(List<SafeZone> safeZoneSubList){
		List<SafeZone> deleteSafeZoneList = new ArrayList<>();
		
		for(SafeZone checkTarget : safeZoneSubList) {
			for(int idx=0; idx<4; idx++) {
				int prev = checkTarget[];
				int next = ;
			}
		}
		return deleteSafeZoneList;
	}
}
