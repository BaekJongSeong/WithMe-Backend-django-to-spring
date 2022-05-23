package com.server.withme.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for AccountOption API
 * AccountOption entity: 1:1 with Account(uuid)
 * option for account
 *
 * @author Jongseong Baek
 */

@Entity
@Getter
@Setter
@Builder
@Table(name = "account_option")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "AccountOption entity: 1:1 with Account(uuid), option for account")
public class AccountOption implements Serializable {
	private static final long serialVersionUID = 1L;

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 
	 @Column(name = "box_size")
	 @ApiModelProperty(notes = "standard for safe_zone divided per user")
	 private Integer boxSize;

	 @ApiModelProperty(notes = "total move for checking velocity")
	 private Double distance;
	 
	 @Column(name = "init_safe_zone")
	 @ApiModelProperty(notes = "checking create safe_zone per user")
	 private Boolean initSafeZone;
	 
	 @Column(name = "safe_move")
	 @ApiModelProperty(notes = "checking safe_move for push alarm could going loosly")
	 private Boolean safeMove;
	 
	 @Column(name = "x_point")
	 @ApiModelProperty(notes = "standard point for latitude")
	 private Double xPoint;
	 
	 @Column(name = "y_point")
	 @ApiModelProperty(notes = "standard point for longitude")
	 private Double yPoint;
	 
	 //cascade = CascadeType.ALL => 상태변화를 전이
	 @OneToMany(mappedBy = "accountOption", fetch=FetchType.LAZY , cascade = CascadeType.ALL)
	 private List<Location> loctionList = new LinkedList<>();
	 
	 @OneToMany(mappedBy = "accountOption", fetch=FetchType.LAZY , cascade = CascadeType.ALL)
	 private List<TTL> ttlList = new LinkedList<>();

	 @OneToMany(mappedBy = "accountOption", fetch=FetchType.LAZY , cascade = CascadeType.ALL)
	 private List<MostVisitPlace> mostVisitPlaceList = new LinkedList<>();
	 
	 @OneToMany(mappedBy = "accountOption", fetch=FetchType.LAZY , cascade = CascadeType.ALL)
	 private List<InitSafeZone> initSafeZoneList = new LinkedList<>();
	 
	 @OneToOne(fetch = FetchType.LAZY)
	 private Account account;
	 
	 public static AccountOption createAccountOptionEntity(Account account) {
		 return AccountOption.builder()
					.boxSize(100)
					.distance(0.0)
					.initSafeZone(false)
					.safeMove(false)
					.xPoint(null)
					.yPoint(null)
					.account(account).build();
	 }
}
