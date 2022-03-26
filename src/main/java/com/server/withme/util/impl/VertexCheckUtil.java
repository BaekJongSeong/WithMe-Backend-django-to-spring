package com.server.withme.util.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.Location;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.LocationDto;
import com.server.withme.model.VertexDto;
import com.server.withme.serivce.ITTLService;
import com.server.withme.util.IVertexCheckUtil;

import lombok.RequiredArgsConstructor;

/**
 * Util for Vertex(check in and out, check min / max vertex)
 *
 * @author Jongseong Baek
 */
@RequiredArgsConstructor
@Component
public class VertexCheckUtil implements IVertexCheckUtil{

	private final ITTLService ttlService;	
	
	//4.23KM 기준으로 움직임
	@Override
	public Map<String,String> checkSafeZoneMinSize(List<VertexDto> safeZoneList) {
		
		Map<String,String> result = new HashMap<>();
		Map<String,Double> minMaxMap = this.findMinMaxVertex(safeZoneList);
		result.put("maxLatitude", String.valueOf(minMaxMap.get("maxLatitude")));
		result.put("minLongitude", String.valueOf(minMaxMap.get("minLongitude")));
		result.put("mixLatitude", String.valueOf(minMaxMap.get("minLatitude")));
		result.put("maxLongitude", String.valueOf(minMaxMap.get("maxLongitude")));
		
	    if (minMaxMap.get("maxLatitude") - minMaxMap.get("minLatitude") > 0.0423) {
	        if (minMaxMap.get("maxLongitude") - minMaxMap.get("minLongitude") > 0.0423) result.put("result", "true");
	        else result.put("result", "false");
	    }
	    else result.put("result", "false");
		return result;
	}
	
	@Override
	public Map<String,Double> findMinMaxVertex(List<VertexDto> safeZoneList){
		
		Map<String,Double> minMaxMap = new HashMap<>();
		
		safeZoneList.sort(Comparator.comparing(VertexDto::getLatitude).reversed());
		minMaxMap.put("maxLatitude",safeZoneList.get(0).getLatitude());
		minMaxMap.put("minLatitude",safeZoneList.get(safeZoneList.size()-1).getLatitude());
		
		safeZoneList.sort(Comparator.comparing(VertexDto::getLongitude).reversed());
		minMaxMap.put("maxLongitude",safeZoneList.get(0).getLongitude());
		minMaxMap.put("minLongitude",safeZoneList.get(safeZoneList.size()-1).getLongitude());
		return minMaxMap;
	}
	
	@Override
	public Integer countSafeZone(List<VertexDto> initSafeZoneListChanged) {
				
		Map<String,Double> minMaxMap = this.findMinMaxVertex(initSafeZoneListChanged);
		
		double x= minMaxMap.get("maxLatitude");
		Double perBoxSize = (double) (100/100000);
		Integer row=0,col=0;
		
		while(x > minMaxMap.get("minLatitude")) {
			
			double y=minMaxMap.get("minLongitude");
			
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
	public int findSafeZoneIndex(VertexDto vertexDto,Map<String,String> resultMap) {
		Double perBoxSize = (double) (100/100000);
		double x = Double.valueOf(resultMap.get("maxLatitude"));
		double y = Double.valueOf(resultMap.get("minLongitude"));
		int row=0,col=0;
		while(row > vertexDto.getLatitude()) {
			x-=perBoxSize;
			row+=1;
		}
		while(col < vertexDto.getLongitude()){
			y+=perBoxSize;
			col+=1;
		}
		return row*(int)((Double.valueOf(resultMap.get("longitude"))-Double.valueOf(resultMap.get("longitude"))) / perBoxSize)+col;
	}
	
	@Override
	public List<List<VertexDto>> checkInAndOutForUpdate(AccountOption accountOption,
			List<VertexDto> vertexDtoList,List<VertexDto> totalList,Map<String,String> resultMap){
		
		List<List<VertexDto>> twoDimensionList = new ArrayList<>();
		List<TTL> deleteTTLList = new ArrayList<>();
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
		for(int idx=0; idx<totalList.size();idx+=4)
			twoDimensionList.add(totalList.subList(idx, idx+4));
		int[] saveTTLList = new int[twoDimensionList.size()];
				
		int index=0;
		for(int idx=0; idx<vertexDtoList.size();idx++) {
			index = this.findSafeZoneIndex(vertexDtoList.get(idx),resultMap);
			saveTTLList[index]=1;
		}
		
		for(int idx=saveTTLList.length-1; idx>-1;idx--) {
			if(saveTTLList[idx]==0) {
				twoDimensionList.remove(idx);
				deleteTTLList.add(ttlList.get(idx));
			}
		}	
		ttlService.deleteAllTTL(deleteTTLList);
		return twoDimensionList;
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
}
