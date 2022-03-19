package com.server.withme.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Account implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    @Column(name = "account_id")
    @Type(type="uuid-char")
    private UUID accountId;
	
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;  

    //id, spring security
    @Column(unique = true)
    private String username;
    
    //password는 항상 jsonignore를 붙여서 절대로 반환되는 일이 없게 해야함.
    @JsonIgnore
    private String password;

    private String name;
    
    private String email;
    
    @Column(name = "email_verified")
    private boolean emailVerified;
    
    @Column(name = "un_locked")
    private boolean unLocked;
    
    private String accountType;
    //권한 목록 => collection은 따로 테이블 파서 거기에 저장 후 join(주소지로 도시 고르듯이)
    //=> 왠만하면 collection 사용하지 말고 1 : N 관계로 테이블 매핑하기 꼭
    //@ElementCollection 
    //@CollectionTable(name = "account_type", joinColumns = @JoinColumn(name="account_id")) 
    //@Column(name = "type")
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
    
    
}
