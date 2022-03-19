package com.server.withme.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for ZoneLocation API
 * ZoneLocation entity: safe zone box list
 * divide init safe zone into smaller box list for customizing
 * 
 * @author Jongseong Baek
 */

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "ZoneLocation entity: safe zone box list, divide init safe zone into smaller box list for customizing")
public class ZoneLocation implements Serializable {
	private static final long serialVersionUID = 1L;

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 
	 private Double latitude;
	    
	 private Double longitude;
	 
	 @ManyToOne
	 private SafeZone safeZone;
}