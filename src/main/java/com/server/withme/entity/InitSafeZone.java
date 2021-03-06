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
 * Entity for InitSafeZone API
 * InitSafeZone entity: creating safe_zone
 * just one box(whole safe zone here)
 *
 * @author Jongseong Baek
 */

@Entity
@Getter
@Setter
@Builder
@Table(name = "init_safe_zone")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "InitSafeZone entity: creating safe_zone, just one box(whole safe zone here)")
public class InitSafeZone implements IVertexDto {
	
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
	 private AccountOption accountOption;
	 
	 public static InitSafeZone createInitSafeZoneEntity(VertexDto safeZone, AccountOption accountOption) {
			return InitSafeZone.builder()
			.latitude(safeZone.getLatitude())
			.longitude(safeZone.getLongitude())
			.accountOption(accountOption).build();
		 }

}
