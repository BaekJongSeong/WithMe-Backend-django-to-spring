package com.server.withme.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.withme.model.SignupDto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for Account API
 * Account entity: include uuid and timestamp (simliar like User)
 *
 * @author Jongseong Baek
 */

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Account entity: include uuid and timestamp (it is simliar like User)")
@Table(name = "account")
public class Account implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue
    @Column(name = "account_id")
    @Type(type="uuid-char")
    private UUID accountId;
	
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;  

    //id, spring security
    @Column(unique = true)
    private String username;
    
    @JsonIgnore
    private String password;

    private String name;
    
    private String email;
    
    private String phone;
    
    @Column(name = "email_verified")
    private Boolean emailVerified;
    
    @Column(name = "un_locked")
    private Boolean unLocked;
    
    private String accountType;
    
    private Collection<GrantedAuthority> authorities;
	    
    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private AccountOption accountOption;
    
    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

    public boolean getEmailVerified() {
    	return this.emailVerified;
    }
    
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return unLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return (emailVerified && unLocked);
	}
	
	 public static Account createAccountEntity(SignupDto signupDto, String password, String role) {
		return Account.builder()
			.createAt(new Timestamp(System.currentTimeMillis()))
         .username(signupDto.getLoginDto().getUsername())
         .password(password)
         .name(signupDto.getName())
         .email(signupDto.getEmail())
         .phone(signupDto.getPhone())
         .emailVerified(false)
         .unLocked(false)
         .accountType(role).build();
	 }
    
    
}
