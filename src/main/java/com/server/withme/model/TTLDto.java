package com.server.withme.model;

import java.util.Date;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
* DTO for TTL API
*
* @author Jongseong Baek
*/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TTLDto {
	
	private Date ttl;
	
	public static TTLDto createTTLDto(Date date) {
		return TTLDto.builder()
				.ttl(date)
				.build();
	}
}
