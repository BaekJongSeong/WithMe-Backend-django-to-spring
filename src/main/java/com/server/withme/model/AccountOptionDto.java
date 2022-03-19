package com.server.withme.model;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;
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
}
