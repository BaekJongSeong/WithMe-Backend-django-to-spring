package com.server.withme.model;

import java.util.List;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* DTO for InitSafeZone API
* creating safe zone based by InitSafeZoneDto
*
* @author Jongseong Baek
*/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SafeZoneDto {
	
	@NotNull
	private List<VertexDto> safeZone;
}
