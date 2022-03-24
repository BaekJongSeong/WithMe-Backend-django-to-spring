package com.server.withme.model;

import com.sun.istack.NotNull;
import lombok.*;

import java.util.UUID;
/**
 * DTO for sendMail API
 * return result place
 *
 * @author Jongseong Baek
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMailDto {

	@NotNull
    private UUID accountId;

    @NotNull
    private String email;
}
