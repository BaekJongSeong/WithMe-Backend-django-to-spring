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
}
