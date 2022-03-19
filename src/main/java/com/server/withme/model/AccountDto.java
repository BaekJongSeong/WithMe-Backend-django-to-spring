package com.server.withme.model;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Date timestamp;  

    @NotNull
    private String username;
    
    @JsonIgnore
    private String password;
    
    private String name;
    
    private String email;
    
    private boolean emailVerified;
    
    private boolean unLocked;
    
    @NotNull
    private String accountType;
}
