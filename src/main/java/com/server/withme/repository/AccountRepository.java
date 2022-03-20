package com.server.withme.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.withme.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByUsername(String username);
    
    Optional<Account> findByAccountId(UUID accountId);
    
    @Query("select DISTINCT c from Account c left join fetch c.accountOption where c.accountId=?1")
    Account findJoinFetch(UUID accountId);
}
