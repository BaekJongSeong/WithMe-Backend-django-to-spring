package com.server.withme.model;

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
    private TTLDto ttlDto;  
	
    @NotNull
    private VertexDto vertexDto;
}
