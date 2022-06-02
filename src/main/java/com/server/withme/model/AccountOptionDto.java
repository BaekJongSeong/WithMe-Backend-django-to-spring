package com.server.withme.model;

import java.util.UUID;

import com.server.withme.entity.AccountOption;
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
	
	 
	 private Integer id;
	 
	 private Integer boxSize;
	 
	 private Double distance;
	 
	 private Boolean initSafeZone;
	 
	 private Boolean safeMove;
	 
	 @NotNull
	 private Double xPoint;
	 
	 @NotNull
	 private Double yPoint;
	 
	 private UUID accountId;
	 
	public static AccountOptionDto createAccountOptionDto(AccountOption accountOption) {
			return AccountOptionDto.builder()
				.id(accountOption.getId())
				.boxSize(accountOption.getBoxSize())
				.distance(accountOption.getDistance())
				.initSafeZone(accountOption.getInitSafeZone())
				.safeMove(accountOption.getSafeMove())
				.xPoint(accountOption.getXPoint())
				.yPoint(accountOption.getYPoint())
				.accountId(accountOption.getAccount().getAccountId())
				.build();
		}
}
