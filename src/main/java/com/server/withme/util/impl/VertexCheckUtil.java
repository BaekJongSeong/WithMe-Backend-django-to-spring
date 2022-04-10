package com.server.withme.util.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
		
	public boolean checkIfTrue(List<VertexDto> vertexDtoList) {
		return (vertexDtoList.get(1).getLongitude() - vertexDtoList.get(0).getLongitude() > 0.0423) ? true: false;
	}
	
	//4.23KM 기준으로 움직임
	@Override
	public List<VertexDto> checkSafeZoneMinSize(List<VertexDto> safeZoneList) {
		
		List<VertexDto> vertexDtoList = this.findMinMaxVertex(safeZoneList);
		boolean TF = (vertexDtoList.get(0).getLatitude() - vertexDtoList.get(1).getLatitude() > 0.0423) ? this.checkIfTrue(vertexDtoList): false;
		vertexDtoList.get(0).setTF(TF);
		vertexDtoList.get(1).setTF(TF);
	
		return vertexDtoList;
	}
	
	@Override
	public List<VertexDto> findMinMaxVertex(List<VertexDto> safeZoneList){
			
		safeZoneList.sort(Comparator.comparing(VertexDto::getLatitude).reversed());
		double maxX = safeZoneList.get(0).getLatitude(); double minX = safeZoneList.get(safeZoneList.size()-1).getLatitude();
		safeZoneList.sort(Comparator.comparing(VertexDto::getLongitude).reversed());
		double maxY = safeZoneList.get(0).getLongitude(); double minY = safeZoneList.get(safeZoneList.size()-1).getLongitude();
		
		VertexDto vertex1 = new VertexDto(maxX,minY, true);
		VertexDto vertex2 = new VertexDto(minX,maxY,true); 		
		return new ArrayList<VertexDto>(Arrays.asList(vertex1,vertex2));
	}
	
	@Override
	public Integer countSafeZone(List<VertexDto> initSafeZoneListChanged) {
				
		List<VertexDto> vertexDtoList = this.findMinMaxVertex(initSafeZoneListChanged);
		
		double x= vertexDtoList.get(0).getLatitude();
		Double perBoxSize = (double) (100/100000);
		Integer row=0,col=0;
		
		while(x > vertexDtoList.get(1).getLatitude()) {
			
			double y=vertexDtoList.get(0).getLongitude();
			
			while(y < vertexDtoList.get(1).getLongitude()) {
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
	public int findSafeZoneIndex(VertexDto vertexDto,List<VertexDto> vertexDtoList) {
		Double perBoxSize = (double) (100/100000);
		double x = Double.valueOf(vertexDtoList.get(0).getLatitude());
		double y = Double.valueOf(vertexDtoList.get(0).getLongitude());
		int row=0,col=0;
		while(row > vertexDto.getLatitude()) {
			x-=perBoxSize;
			row+=1;
		}
		while(col < vertexDto.getLongitude()){
			y+=perBoxSize;
			col+=1;
		}
		return row*(int)((vertexDtoList.get(1).getLongitude()-vertexDtoList.get(0).getLongitude()) / perBoxSize)+col;
	}
	
	@Override
	public List<List<VertexDto>> checkInAndOutForUpdate(AccountOption accountOption,
			List<VertexDto> vertexDtoList,List<VertexDto> totalList,List<VertexDto> vertexDto){
		
		List<List<VertexDto>> twoDimensionList = new ArrayList<>();
		List<Integer> deleteTTLList = new ArrayList<>();
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
		
		for(int idx=0; idx<totalList.size();idx+=4)
			twoDimensionList.add(totalList.subList(idx, idx+4));
		int[] saveTTLList = new int[twoDimensionList.size()];
				
		int index=0;
		for(int idx=0; idx<vertexDtoList.size();idx++) {
			index = this.findSafeZoneIndex(vertexDtoList.get(idx),vertexDto);
			saveTTLList[index]=1;
		}
		
		for(int idx=saveTTLList.length-1; idx>-1;idx--) {
			if(saveTTLList[idx]==0) {
				twoDimensionList.remove(idx);
				deleteTTLList.add(ttlList.get(idx).getId());
			}
		}	
		ttlService.deleteAllTTLById(deleteTTLList);
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
	public Boolean checkInAndOutLocation(SafeZone target, List<List<SafeZone>> totalSafeZoneList,List<TTL> ttlList){
		int status = 0, countVertex=0;
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
		return status==1 ? true : false;
	}
	
	@Override
	public Boolean checkLatestLocation(LocationDto locationDto,Location location) {
		if(Math.abs(location.getLatitude() - locationDto.getVertexDto().getLatitude()) < 0.00001 && 
				Math.abs(location.getLongitude() - locationDto.getVertexDto().getLongitude()) < 0.00001)
					return false;
		return true;
	}
}
