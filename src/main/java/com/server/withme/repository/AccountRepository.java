package com.server.withme.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.withme.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByUsername(String username);
    
    Optional<Account> findByAccountId(UUID accountId);
}
