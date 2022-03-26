package com.server.withme.serivce.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.server.withme.entity.Account;
import com.server.withme.entity.AccountOption;
import com.server.withme.entity.InitSafeZone;
import com.server.withme.entity.SafeZone;
import com.server.withme.entity.TTL;
import com.server.withme.model.AccountIdDto;
import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneDto;
import com.server.withme.model.SafeZoneInfoDto;
import com.server.withme.model.VertexDto;
import com.server.withme.repository.AccountOptionRepository;
import com.server.withme.repository.InitSafeZoneRepository;
import com.server.withme.repository.SafeZoneRepository;
import com.server.withme.repository.TTLRepository;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.ILocationService;
import com.server.withme.serivce.ISafeZoneService;
import com.server.withme.serivce.ITTLService;
import com.server.withme.util.IVertexCheckUtil;
import com.server.withme.util.IVertexUtil;

import lombok.RequiredArgsConstructor;
/**
 * Service for SafeZone
 *
 * @author Jongseong Baek
 */

@RequiredArgsConstructor
@Service
public class SafeZoneService implements ISafeZoneService{
	
	private final SafeZoneRepository safeZoneRepository;
	
	private final InitSafeZoneRepository initSafeZoneRepository;
	
	private final AccountOptionRepository accountOptionRepository;
	
	private final TTLRepository ttlRepository;
		
	private final ISafeZoneService safeZoneService;
	
	private final IAccountOptionService accountOptionService;
	
	private final ILocationService locationService;
	
	private final ITTLService ttlService;
	
	private final IVertexUtil vertexUtil;
	
	private final IVertexCheckUtil vertexCheckUtil;
	
	@Override
	public List<VertexDto> saveInitSafeZone(SafeZoneDto safeZoneDto, UUID accountId) {
		
		List<VertexDto> initSafeZoneList = safeZoneDto.getSafeZone();
		Map<String,String> result = vertexCheckUtil.checkSafeZoneMinSize(initSafeZoneList);
		if(result.get("result").equals("false")) {
			initSafeZoneList.add(VertexDto.builder().latitude(0.0).build());
			return initSafeZoneList;
		}
		AccountOption accountOption = accountOptionService.updateAccountOption(
				accountId,Double.valueOf(result.get("maxLatitude")),Double.valueOf(result.get("minLongitude")));
		
		for(VertexDto vertex: initSafeZoneList) {
			initSafeZoneRepository.save(InitSafeZone.builder()
					.latitude(vertex.getLatitude())
					.longitude(vertex.getLongitude())
					.accountOption(accountOption).build());
		}
		initSafeZoneList.add(VertexDto.builder().latitude(1.0).build());
		return initSafeZoneList;
	}
	
	@Override
	public List<VertexDto> saveSafeZoneFirstTime(SafeZoneDto safeZoneDto, UUID accountId) {
		
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		List<VertexDto> initSafeZoneList = safeZoneDto.getSafeZone();
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());

		List<VertexDto> safeZoneList = vertexUtil.calculateVertex(initSafeZoneList);
		
		int count=0;
		for(TTL ttl: ttlList) {
			while(true) {
				if(count% 4 == 0 && count != 0)
					break;
				//spring batch bulk로 create하기
				safeZoneRepository.save(SafeZone.builder()
					.latitude(safeZoneList.get(count).getLatitude())
					.longitude(safeZoneList.get(count).getLongitude())
					.ttl(ttl).build());	
				count++;
			}
		}
		return safeZoneList;
	}
	
	@Override
	public List<VertexDto> deleteSafeZoneFirstTime(UUID accountId) {
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		List<TTL> ttlList= ttlService.findByAccountOptionIdOrThrow(accountOption.getId());
		
		List<SafeZone> safeZoneList = new ArrayList<>();
		for(TTL ttl: ttlList) {
			List<SafeZone> tempSafeZoneList = safeZoneService.findByTTLIdOrThrow(ttl.getId());
			safeZoneList.addAll(tempSafeZoneList);
		}
		
		List<SafeZone> deleteSafeZoneList = vertexUtil.calculateDeleteVertex(safeZoneList,accountOption);
		ttlList.clear();
		
		for(int idx=0; idx<deleteSafeZoneList.size();idx+=4)
			ttlList.add(deleteSafeZoneList.get(idx).getTtl());
		
		ttlService.deleteAllTTL(ttlList);
		
		return vertexUtil.convertSafeZoneToVertexDto(deleteSafeZoneList);
	}
	
	@Override
	public List<VertexDto> createSafeZoneByLocation(UUID accountId,LocationDto locationDto){
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountId);
		TTL ttl = ttlService.saveTTL(accountOption);
		List<VertexDto> newSafeZoneList = vertexUtil.createSafeZoneByLocation(accountOption,locationDto);
		
		for(VertexDto safeZone: newSafeZoneList)
			safeZoneRepository.save(SafeZone.builder()
					.latitude(safeZone.getLatitude())
					.longitude(safeZone.getLongitude())
					.ttl(ttl).build());	
		
		return newSafeZoneList;
	}
	
	@Override
	public void updateSafeZone(List<Account> checkedAccountList) {
		List<AccountOption> accountOptionList = accountOptionService.findAllFetchAccountOption(checkedAccountList);
		
		for(AccountOption accountOption: accountOptionList) {
			List<VertexDto> locationList = vertexUtil.convertLocationToVertexDto(
					locationService.findByAccountOptionIdOrThrow(accountOption.getId()));
			
			Map<String,String> resultMap = vertexCheckUtil.checkSafeZoneMinSize(locationList);
			
			if(resultMap.get("result").equals("true")) {
				accountOption = accountOptionService.updateAccountOption(accountOption.getAccount().getAccountId(),
						Double.valueOf(resultMap.get("latitude")),Double.valueOf(resultMap.get("longitude")));
				
				List<VertexDto> vertexDtoList= vertexUtil.calculateVertex(locationList);
				ttlService.deleteAllTTL(ttlService.findByAccountOptionIdOrThrow(accountOption.getId()));	
				ttlService.saveTTLFirstTime(vertexDtoList,accountOption);
				
				locationList = vertexCheckUtil.checkInAndOutForUpdate(accountOption,locationList, vertexDtoList, resultMap);
			}
		}
	}
	
	@Override
	public void saveSafeZoneAll(List<VertexDto> vertexDtoList, List<TTL> ttlList) {
		int index=0;
		for(int idx=0; idx<vertexDtoList.size();idx++) {
			if(idx%4==0 &&idx!=0)
				index++;
				
			safeZoneRepository.save(SafeZone.builder()
					.latitude(vertexDtoList.get(idx).getLatitude())
					.longitude(vertexDtoList.get(idx).getLongitude())
					.ttl(ttlList.get(index)).build());
		}
	}
	
	@Override
	public List<InitSafeZone> loadInitSafeZoneList(AccountIdDto accountIdDto){
		AccountOption accountOption = accountOptionService.findByAccountIdOrThrow(accountIdDto.getAccountId());
		return this.findByAccountOptionIdOrThrow(accountOption.getId());
	}
	
	@Override
	public <T> SafeZoneInfoDto<T> craeteSafeZoneInfoDto(List<T> list, double trueOrFalse, int flag){
		SafeZoneInfoDto<T> safeZoneInfoDto = new SafeZoneInfoDto<>();
		
		if(trueOrFalse> 0.0)
			if(flag == 1)
				safeZoneInfoDto.setMessage("location에 해당하는 safeZone의 TTL이 업데이트 되었습니다.");
			else
				safeZoneInfoDto.setMessage("등록이 완료되었습니다.");
		else
			if(flag == 1)
				safeZoneInfoDto.setMessage("새로운 safeZone이 생성되었습니다.");
			else
			safeZoneInfoDto.setMessage("최소 size 또는 같은 위치 반복을 위반하여 등록되지 않았습니다.");
		
		safeZoneInfoDto.setData(list);
		return safeZoneInfoDto;
	}

	@Override
	public List<InitSafeZone> findByAccountOptionIdOrThrow(Integer accountOptionId){
		AccountOption accountOption= accountOptionRepository.findByFetchInitSafeZone(accountOptionId).orElseThrow(()
							-> new UsernameNotFoundException("not found accountOption"));
	
		return accountOption.getInitSafeZoneList();
	}
	
	@Override
	public List<SafeZone> findByTTLIdOrThrow(Integer ttlId){
		TTL ttl= ttlRepository.findByFetchSafeZone(ttlId).orElseThrow(()
							-> new UsernameNotFoundException("not found ttl"));
	
		return ttl.getSafeZoneList();
	}
}
