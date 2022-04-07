package com.server.withme.serivce.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.server.withme.entity.Account;
import com.server.withme.entity.AccountOption;
import com.server.withme.entity.TTL;
import com.server.withme.model.VertexDto;
import com.server.withme.serivce.IAccountOptionService;
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
	
	private final IAccountOptionService accountOptionService;
	
	private final ILocationService locationService;
	
	private final ISafeZoneService safeZoneService;
	
	private final ITTLService ttlService;
	
	private final IVertexCheckUtil vertexCheckUtil;
	
	private final IVertexUtil vertexUtil;
	
	
	@Override
	public void updateSafeZoneBySchedular(List<Account> checkedAccountList) {
		List<AccountOption> accountOptionList = accountOptionService.findAllFetchAccountOption(checkedAccountList);
		
		for(AccountOption accountOption: accountOptionList) {
			List<VertexDto> locationList = vertexUtil.convertLocationToVertexDto(
					locationService.findByAccountOptionIdOrThrow(accountOption.getId()));
			
			List<VertexDto> vertexDto = vertexCheckUtil.checkSafeZoneMinSize(locationList);
			
			if(vertexDto.get(0).getTF()) {
				accountOption = accountOptionService.updateAccountOption(accountOption.getAccount().getAccountId(),
						vertexDto.get(0).getLatitude(),vertexDto.get(0).getLongitude());
				
				List<VertexDto> vertexDtoList= vertexUtil.calculateVertex(locationList);
				List<TTL> ttlList = ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
				
				List<Integer> ttlIdList = new ArrayList<>();
				for(TTL ttl : ttlList)
					ttlIdList.add(ttl.getId());
				ttlService.deleteAllTTLById(ttlIdList);	
				
				ttlService.saveTTLFirstTime(vertexDtoList,accountOption);
				safeZoneService.saveSafeZoneFirstTime(vertexDtoList, accountOption.getAccount().getAccountId());
				List<List<VertexDto>> finalSafeZoneList = vertexCheckUtil.
						checkInAndOutForUpdate(accountOption,locationList, vertexDtoList, vertexDto);
				
				System.out.println("this is now updated safeZone\n"+finalSafeZoneList.toString());
			}
		}
	}
	
}
