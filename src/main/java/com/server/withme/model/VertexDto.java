package com.server.withme.model;

import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* DTO for Vertex API
* latitude and longitude in vertex
*
* @author Jongseong Baek
*/
@Getter
@Setter
@Builder
@NoArgsConstructor
public class VertexDto {
	@NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
    
    private boolean TF;
    
    public VertexDto(Double latitude, Double longitude) {
    	super();
    	this.latitude = latitude;
    	this.longitude = longitude;
    }
    
    public VertexDto(Double latitude, Double longitude,boolean TF) {
    	this(latitude,longitude);
    	this.TF=TF;
    }
    
    public boolean getTF() {
    	return this.TF;
    }
}
