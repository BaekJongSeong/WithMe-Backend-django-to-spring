package com.server.withme.model;

import com.server.withme.entity.Account;
import com.server.withme.entity.Location;
import com.sun.istack.NotNull;
import lombok.*;

/**
 * DTO for signup API
 *
 * @author Jongseong Baek
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto{

    @NotNull
    private LoginDto loginDto;

    private String name;

    private String email;
    
    private String phone;
    
    private String accountType;
    
    public static SignupDto createSignupDto(Account account) {
		return SignupDto.builder()
			    .loginDto(LoginDto.builder().username(account.getUsername()).build())
			    .name(account.getName())
			    .email(account.getEmail())
			    .phone(account.getPhone())
			    .accountType(account.getAccountType()).build();
	}
}
