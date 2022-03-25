package com.server.withme.model;

import java.util.Date;
import java.util.List;

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
	
	@NotNull
	private Date ttl;
}
