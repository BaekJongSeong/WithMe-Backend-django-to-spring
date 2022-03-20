package com.server.withme.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
public class InitSafeZone implements Serializable {
	private static final long serialVersionUID = 1L;

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 
	 private Double latitude;
	 
	 private Double longitude;
	 
	 @ManyToOne
	 private AccountOption accountOption;

}
