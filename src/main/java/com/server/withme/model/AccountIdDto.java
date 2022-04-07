package com.server.withme.model;

import com.sun.istack.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * DTO for AccountID API
 * using for vertify
 * 
 * @author Jongseong Baek
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountIdDto {

    @NotNull
    private UUID accountId;
}
