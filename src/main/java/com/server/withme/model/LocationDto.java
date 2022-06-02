package com.server.withme.model;

import com.server.withme.entity.Location;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for Location API
 *
 * @author Jongseong Baek
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
	
	@NotNull
	private String name;
	
    private TTLDto ttlDto;  
	
    @NotNull
    private VertexDto vertexDto;
    
    public static LocationDto createLocationDto(Location location) {
		return LocationDto.builder()
			    .name(location.getName())
			    .vertexDto(new VertexDto(location.getLatitude(),location.getLongitude()))
			    .build();
	}
    
}
