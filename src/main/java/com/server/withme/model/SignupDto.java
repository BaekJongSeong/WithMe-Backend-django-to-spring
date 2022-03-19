package com.server.withme.model;

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
public class SignupDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String email;
    
    @NotNull
    private String accountType;
}
