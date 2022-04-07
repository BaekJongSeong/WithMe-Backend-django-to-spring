package com.server.withme.util.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.SafeZone;
import com.server.withme.enumclass.IVertexDto;
import com.server.withme.model.LocationDto;
import com.server.withme.model.VertexDto;
import com.server.withme.serivce.ILocationService;
import com.server.withme.serivce.ISafeZoneService;
import com.server.withme.util.IVertexCheckUtil;
import com.server.withme.util.IVertexUtil;

import lombok.RequiredArgsConstructor;
/**
 * Util for Vertex(create / calculate vertex)
 *
 * @author Jongseong Baek
 */
@RequiredArgsConstructor
@Component
public class VertexUtil implements IVertexUtil{
		
	private final ISafeZoneService safeZoneService;
	
	private final ILocationService locationService;
		
	private final IVertexCheckUtil vertexCheckUtil;
	
	@Override
	public <T extends IVertexDto> List<VertexDto> convertToVertexDto(List<T> list){
		List<VertexDto> vertexDtoList = new ArrayList<>();
		for(T item : list)
			vertexDtoList.add(locationService.createVertexDto(item.getLatitude(),item.getLongitude(),true));
		return vertexDtoList;
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
	public List<VertexDto> createNewSafeZone(List<VertexDto> safeZoneList, double x, double y, double perBoxSize) {
		safeZoneList.add(new VertexDto(x , y));
		safeZoneList.add(new VertexDto(x , y + perBoxSize));
		safeZoneList.add(new VertexDto(x - perBoxSize , y + perBoxSize));
		safeZoneList.add(new VertexDto(x - perBoxSize , y));
		return safeZoneList;
	}
	
	@Override
	public List<VertexDto> calculateVertex(List<VertexDto> initSafeZoneList){
		
		List<VertexDto> safeZoneList = new ArrayList<>();
		List<VertexDto> vertexDtoList = vertexCheckUtil.findMinMaxVertex(initSafeZoneList);
		
		double x= vertexDtoList.get(0).getLatitude();
		Double perBoxSize = (double) (100/100000);
		
		while(x > vertexDtoList.get(1).getLatitude()) {
			
			double y=vertexDtoList.get(0).getLongitude();
			
			while(y < vertexDtoList.get(1).getLongitude()) {
				this.createNewSafeZone(safeZoneList,x,y,perBoxSize);
				y += perBoxSize;
			}
			x += perBoxSize;
		}
		return safeZoneList;
	}
	
	@Override
	public List<VertexDto> createSafeZoneByLocation(AccountOption accountOption, LocationDto locationDto) {
		List<VertexDto> safeZoneList = new ArrayList<>();
		Double perBoxSize = (double) (100/100000);
			
		if(accountOption.getXPoint() < locationDto.getVertexDto().getLatitude()) {
			double x= accountOption.getXPoint();
			while(x > locationDto.getVertexDto().getLatitude())
				x += perBoxSize;
				
			if(accountOption.getYPoint() > locationDto.getVertexDto().getLongitude()) {
				double y=accountOption.getYPoint();
				while(y < locationDto.getVertexDto().getLongitude())
						y -= perBoxSize;
						
				createNewSafeZone(safeZoneList,x,y,perBoxSize);
			}
				
			else {
				double y=accountOption.getYPoint();
				while(y < locationDto.getVertexDto().getLongitude())
						y += perBoxSize;
						
				createNewSafeZone(safeZoneList,x,y,perBoxSize);
			}
		}
		else {
			double x= accountOption.getXPoint();
			while(x > locationDto.getVertexDto().getLatitude())
				x -= perBoxSize;
				
			if(accountOption.getYPoint() > locationDto.getVertexDto().getLongitude()) {
				double y=accountOption.getYPoint();
				while(y < locationDto.getVertexDto().getLongitude())
						y -= perBoxSize;
						
				createNewSafeZone(safeZoneList,x,y,perBoxSize);
			}
				
			else {
				double y=accountOption.getYPoint();
				while(y < locationDto.getVertexDto().getLongitude())
						y += perBoxSize;
						
				createNewSafeZone(safeZoneList,x,y,perBoxSize);
			}
		}
		return safeZoneList;
	}
	
	@Override
	public List<SafeZone> calculateDeleteVertex(List<SafeZone> safeZoneList,AccountOption accountOption){
		List<SafeZone> deleteSafeZoneList = new ArrayList<>();
		List<InitSafeZone> initSafeZoneList= safeZoneService.findByAccountOptionIdOrThrow(accountOption.getId());
		List<SafeZone> initSafeZoneListChanged = this.convertInitSafeZoneToSafeZone(initSafeZoneList);
		
		for(int idx=0; idx<safeZoneList.size(); idx+=4)
			deleteSafeZoneList.addAll(vertexCheckUtil.checkInAndOutInitSafeSone(safeZoneList.subList(idx, idx+4), initSafeZoneListChanged));			
		
		return deleteSafeZoneList;
	}
}
