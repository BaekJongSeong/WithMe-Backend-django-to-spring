package com.server.withme.util.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.Location;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.LocationDto;
import com.server.withme.model.VertexDto;
import com.server.withme.serivce.ISafeZoneService;
import com.server.withme.serivce.ITTLService;
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
		
	private final ISafeZoneService safeZoneService;
	
	private final ITTLService ttlService;
	
	//4.23KM 기준으로 움직임
	@Override
	public Map<String,String> checkSafeZoneMinSize(List<VertexDto> initSafeZoneList) {
		
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
		
		List<InitSafeZone> initSafeZoneList= safeZoneService.findByAccountOptionIdOrThrow(accountOption.getId());
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
	public List<SafeZone> convertInitSafeZoneToSafeZone(List<InitSafeZone> initSafeZoneList){
		List<SafeZone> safeZoneListChanged = new ArrayList<>();
		for(InitSafeZone initSafeZone : initSafeZoneList) {
			
			SafeZone vertexDto = SafeZone.builder()
				.latitude(initSafeZone.getLatitude())
				.longitude(initSafeZone.getLongitude()).build();
		
			safeZoneListChanged.add(vertexDto);
		}
		return safeZoneListChanged;
	}
	
	@Override
	public List<VertexDto> convertSafeZoneToVertexDto(List<SafeZone> safeZoneList){
		
		List<VertexDto> safeZoneListChanged = new ArrayList<>();
		
		for(SafeZone initSafeZone : safeZoneList) {
			
			VertexDto vertexDto = VertexDto.builder()
				.latitude(initSafeZone.getLatitude())
				.longitude(initSafeZone.getLongitude()).build();
		
			safeZoneListChanged.add(vertexDto);
		}
		
		return safeZoneListChanged;
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
	public List<SafeZone> calculateDeleteVertex(List<SafeZone> safeZoneList,AccountOption accountOption){
		List<SafeZone> deleteSafeZoneList = new ArrayList<>();
		List<InitSafeZone> initSafeZoneList= safeZoneService.findByAccountOptionIdOrThrow(accountOption.getId());
		List<SafeZone> initSafeZoneListChanged = convertInitSafeZoneToSafeZone(initSafeZoneList);
		
		for(int idx=0; idx<safeZoneList.size(); idx+=4)
			deleteSafeZoneList.addAll(this.checkInAndOutInitSafeSone(safeZoneList.subList(idx, idx+4), initSafeZoneListChanged));			
		
		return deleteSafeZoneList;
	}
	
	@Override
	public int[] checkInAndOut(SafeZone target, List<SafeZone> totalSafeZoneList){
		int[] result = new int[2];
		
		for(int idx=0; idx<totalSafeZoneList.size(); idx++) {
			SafeZone prev = totalSafeZoneList.get(totalSafeZoneList.size()-1);
			SafeZone next = totalSafeZoneList.get(0);
			if(idx==0) {
				prev = totalSafeZoneList.get(idx-1);
				next = totalSafeZoneList.get(idx);	
			}
					
	        if ((next.getLongitude() < target.getLongitude() && prev.getLongitude() >= target.getLongitude()) || 
	             (prev.getLongitude() < target.getLongitude() && next.getLongitude() >= target.getLongitude()))
	            if (next.getLatitude()+(target.getLongitude()-next.getLongitude()) / 
	               (prev.getLongitude()-next.getLongitude()) * (prev.getLatitude()-next.getLatitude()) < target.getLatitude()) {
	                   result[0] = 1;
	                   result[1] += 1;
	                 }          			
		}
		return result;
	}
	
	@Override
	public List<SafeZone> checkInAndOutInitSafeSone(List<SafeZone> targetList, List<SafeZone> totalSafeZoneList){
		List<SafeZone> deleteSafeZoneList = new ArrayList<>();
		int countVertex=0;
		int status = 0;
		
		for(SafeZone target : targetList) {
			
			int [] result = this.checkInAndOut(target,totalSafeZoneList);
			
			countVertex++;
			status = result[0];
			
			if (status != 1 || result[1] % 2 == 0)  status=2;
            if (status == 1) break;
            
			if(countVertex % 4 == 0) {
				if (status == 2) deleteSafeZoneList.addAll(targetList);
	            status = 0;
				}
			}
		return deleteSafeZoneList;
	}

	@Override
	public List<SafeZone> checkInAndOutLocation(SafeZone target, List<List<SafeZone>> totalSafeZoneList,List<TTL> ttlList){
		List<SafeZone> safeZoneList = new ArrayList<>();
		int status = 0;
		int countVertex=0;
		for(List<SafeZone> totalSafeZone: totalSafeZoneList) {

			int[] result = checkInAndOut(target, totalSafeZone);
			countVertex++;
			status = result[0];
			int count= result[1];
			
            if (count % 2 == 0) status=2;
            if (status == 1) {
            	ttlService.ttlUpdate(ttlList.get(countVertex),countVertex);
            	break;
			}
		}
		//signing
		safeZoneList.add(SafeZone.builder().latitude((double) status).build());
		return safeZoneList;
	}
	
	@Override
	public Boolean checkLatestLocation(LocationDto locationDto,Location location) {
		if(Math.abs(location.getLatitude() - locationDto.getVertexDto().getLatitude()) < 0.00001 && 
				Math.abs(location.getLongitude() - locationDto.getVertexDto().getLongitude()) < 0.00001)
					return false;
		return true;
	}
	
	@Override
	public public List<SafeZone> createSafeZoneByLocation(AccountOption accountOption){
		
	}
}
