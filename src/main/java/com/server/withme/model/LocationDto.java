package com.server.withme.model;

import java.util.Date;

import com.sun.istack.NotNull;
import lombok.*;

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
    private Date timestamp;  
	
    @NotNull
    private String latitude;

    @NotNull
    private String longitude;
}
