package com.server.withme.model;

import java.util.List;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for SafeZoneInfo API
 *
 * @author Jongseong Baek
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SafeZoneInfoDto<T> {

	@NotNull
	private String message;
	
	@NotNull
	private List<T> Data;
	
	public static <T> SafeZoneInfoDto<T> craeteSafeZoneInfoDto(List<T> list, boolean trueOrFalse, int flag){
		SafeZoneInfoDto<T> safeZoneInfoDto = new SafeZoneInfoDto<>();
		String message="";
		
		if(trueOrFalse) 
			message = (flag==1) ? "location에 해당하는 safeZone의 TTL이 업데이트 되었습니다." : "등록이 완료되었습니다.";
		 else 
			message = (flag==1) ? "새로운 safeZone이 생성되었습니다." : "최소 size 또는 같은 위치 반복을 위반하여 등록되지 않았습니다.";
		
		safeZoneInfoDto.setMessage(message);
		safeZoneInfoDto.setData(list);
		return safeZoneInfoDto;
	}
}
