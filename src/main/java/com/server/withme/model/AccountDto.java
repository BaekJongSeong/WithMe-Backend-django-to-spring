package com.server.withme.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.withme.entity.Account;
import com.server.withme.entity.Location;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * DTO for Account API
 *
 * @author Jongseong Baek
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private UUID accountId;

    @NotNull
    private Date createAt;  

    private String username;
    
    @JsonIgnore
    private String password;
    
    private String name;
    
    private String email;
    
    private boolean emailVerified;
    
    private boolean unLocked;
        
    private String accountType;
    
    public static AccountDto createAccountDto(Account account) {
		return AccountDto.builder()
				.accountId(account.getAccountId())
			    .createAt(account.getCreateAt())
			    .username(account.getUsername())
			    .name(account.getName())
			    .email(account.getEmail())
			    .emailVerified(account.getEmailVerified())
			    .accountType(account.getAccountType()).build();
	}
}
