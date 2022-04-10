package com.server.withme.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.withme.enumclass.IVertexDto;
import com.server.withme.model.LocationDto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for Location API
 * Location entity: point for latitude and longitude(insert per 5 second)
 * major thing for processing safe zone (extend and resizing)
 *
 * @author Jongseong Baek
 */

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Lcoation entity: point for latitude and longitude, insert per 5 second, major thing for processing safe zone (extend and resizing)")
public class Location implements Serializable, IVertexDto{
	private static final long serialVersionUID = 1L;

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;  
    
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
    
    @ManyToOne
    private AccountOption accountOption;
    
	 public static Location createLocationEntity(LocationDto locationDto, AccountOption accountOption) {
	    return Location.builder()
		.timestamp(locationDto.getTtlDto().getTtl())
		.latitude(locationDto.getVertexDto().getLatitude())
		.longitude(locationDto.getVertexDto().getLongitude())
		.accountOption(accountOption)
		.build();
	 }
}
