package com.server.withme.model;

import com.sun.istack.NotNull;
import lombok.*;

/**
 * DTO for BestVisitPlace API
 * tracking user data and processing result
 *
 * @author Jongseong Baek
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MostVisitPlaceDto {

	@NotNull
	private Integer grade;
	
    @NotNull
    private String place;

    @NotNull
	private SafeZoneDto safeZoneDto;
}
