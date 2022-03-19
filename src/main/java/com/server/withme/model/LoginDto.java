package com.server.withme.model;

import com.sun.istack.NotNull;
import lombok.*;

/**
 * DTO for Login API
 *
 * @author Jongseong Baek
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
