package com.server.withme.serivce.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.server.withme.entity.AccountOption;
import com.server.withme.entity.TTL;
import com.server.withme.model.VertexDto;
import com.server.withme.repository.AccountOptionRepository;
import com.server.withme.serivce.ILocationService;
import com.server.withme.serivce.ISafeZoneService;
import com.server.withme.serivce.ISchedularService;
import com.server.withme.serivce.ITTLService;
import com.server.withme.util.IVertexCheckUtil;
import com.server.withme.util.IVertexUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service for Schedular
 *
 * @author Jongseong Baek
 */

@RequiredArgsConstructor
@Service
public class SchedularService implements ISchedularService{
		
	private final AccountOptionRepository accountOptionRepository;
	
	private final ILocationService locationService;
	
	private final ISafeZoneService safeZoneService;
	
	private final ITTLService ttlService;
	
	private final IVertexCheckUtil vertexCheckUtil;
	
	private final IVertexUtil vertexUtil;
	
	
	@Override
	@Transactional
	public void updateSafeZoneBySchedular(List<AccountOption> accountOptionList) {		
		for(AccountOption accountOption: accountOptionList) {
			List<VertexDto> locationList = vertexUtil.convertToVertexDto(
					locationService.findByAccountOptionIdOrThrow(accountOption.getId()));
			
			List<VertexDto> vertexDto = vertexCheckUtil.checkSafeZoneMinSize(locationList);
			
			if(vertexDto.get(0).getTF()) {
				//accountOption = 
				accountOptionRepository.updateQuery(vertexDto.get(0).getLatitude(),
						vertexDto.get(0).getLongitude(),accountOption.getId());
				
				List<VertexDto> vertexDtoList= vertexUtil.calculateVertex(locationList);
				List<TTL> ttlList = ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
				
				List<Integer> ttlIdList = new ArrayList<>();
				for(TTL ttl : ttlList)
					ttlIdList.add(ttl.getId());
				ttlService.deleteAllTTLById(ttlIdList);	
				
				ttlService.saveTTLFirstTime(vertexDtoList,accountOption);
				safeZoneService.saveSafeZoneFirstTime(vertexDtoList, accountOption);
				List<List<VertexDto>> finalSafeZoneList = vertexCheckUtil.
						checkInAndOutForUpdate(accountOption,locationList, vertexDtoList, vertexDto);
				
				System.out.println("this is now updated safeZone\n"+finalSafeZoneList.toString());
			}
		}
	}
	
	@Override
	@Transactional
	public void deleteExpireTTLBySchedular(List<AccountOption> accountOptionList) {
		for(AccountOption accountOption: accountOptionList) {
			List<TTL> ttlList = ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
			
			List<List<VertexDto>> safeZoneList = new ArrayList<>();
			List<Integer> ttlIdList = new ArrayList<>();
			for(TTL ttl: ttlList)
				safeZoneList.add(vertexUtil.convertToVertexDto(safeZoneService.findByTTLIdOrThrow(ttl.getId())));
			
			int count=0;double x=0,y=0;
			for(int idx=0; idx< ttlList.size();idx++) {
				if(ttlList.get(idx).getTtl().getTime() < new Timestamp(System.currentTimeMillis()).getTime()) {
					count++;
					safeZoneList.remove(idx);ttlIdList.add(ttlList.get(idx).getId());
					
					List<VertexDto> resultDto = vertexCheckUtil.checkSafeZoneMinSize(
							safeZoneList.stream()
					        .flatMap(List::stream)
					        .collect(Collectors.toList()));
					
					x=resultDto.get(0).getLatitude();
					y=resultDto.get(0).getLongitude();
					if(count %50 ==0 && count!=0 && !resultDto.get(0).getTF())
						break;			
				}
			}
			ttlService.deleteAllTTLById(ttlIdList);
			accountOptionRepository.updateQuery(x,y,accountOption.getId());
		}
	}
}
