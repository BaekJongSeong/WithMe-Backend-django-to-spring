package com.server.withme.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * Entity for MostVisitPlace API
 * MostVisitPlace entity: safe zone box list,
 * the most place where user visited before
 * 
 * @author Jongseong Baek
 */

@Entity
@Getter
@Setter
@Builder
@Table(name = "most_visit_place")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "MostVisitPlace entity: safe zone box list, the most place where user visited before")
public class MostVisitPlace implements Serializable {
	private static final long serialVersionUID = 1L;

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 
	 private Integer grade;
	 
	 private Double latitude;
	    
	 private Double longitude;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 private AccountOption accountOption;
}
