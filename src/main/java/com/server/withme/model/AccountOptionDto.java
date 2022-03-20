package com.server.withme.model;

import java.util.UUID;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for AccountOption API
 *
 * @author Jongseong Baek
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountOptionDto {
	
	 @NotNull
	 private Integer id;
	 
	 private Integer boxSize;
	 
	 private Double distance;
	 
	 @NotNull
	 private Boolean initSafeZone;
	 
	 private Boolean safeMove;
	 
	 @NotNull
	 private Double xPoint;
	 
	 @NotNull
	 private Double yPoint;
	 
	 @NotNull
	 private UUID accountId;
}
