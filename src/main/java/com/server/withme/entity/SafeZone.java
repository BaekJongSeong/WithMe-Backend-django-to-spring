package com.server.withme.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.server.withme.enumclass.IVertexDto;
import com.server.withme.model.VertexDto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for SafeZone API
 * SafeZone entity: safe zone box list
 * divide init safe zone into smaller box list for customizing
 * 
 * @author Jongseong Baek
 */

@Entity
@Setter
@Getter
@Builder
@Table(name = "safe_zone")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "SafeZone entity: safe zone box list, divide init safe zone into smaller box list for customizing")
public class SafeZone implements Serializable, IVertexDto {
	private static final long serialVersionUID = 1L;

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 
	 private Double latitude;
	    
	 private Double longitude;
	 
	 @Override
	 public double getLatitude() {
		 return latitude;
	 }
	 @Override	
	 public double getLongitude() {
		 return longitude;
	 }
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 private TTL ttl;
	 
	 public static SafeZone createSafeZoneEntity(VertexDto safeZone, TTL ttl) {
		return SafeZone.builder()
		.latitude(safeZone.getLatitude())
		.longitude(safeZone.getLongitude())
		.ttl(ttl).build();
	 }
}