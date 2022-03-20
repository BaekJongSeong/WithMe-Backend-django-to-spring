package com.server.withme.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for TTL API
 * TTL entity: ttl for safe zone box list(purpose: user customizing)
 * more visit -> longer ttl, less visit -> opposite
 *
 * @author Jongseong Baek
 */

@Entity
@Getter
@Setter
@Builder
@Table(name = "ttl")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "ttl for safe zone box list, more visit -> longer ttl, less visit -> opposite, for user customizing")
public class TTL implements Serializable {
	private static final long serialVersionUID = 1L;

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 
	 @Column(name = "timestamp")
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	 @ApiModelProperty(notes = "setting safe zone list expired time, limit 7 days")
	 private Date ttl;  
	 
	 @OneToMany(mappedBy="safeZone", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	 private List<SafeZone> safeZoneList = new LinkedList<>();
	 
	 @ManyToOne
	 private AccountOption accountOption;
}
