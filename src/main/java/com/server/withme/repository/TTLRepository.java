package com.server.withme.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.withme.entity.TTL;

public interface TTLRepository extends JpaRepository<TTL, Integer>{
	
	@Query("select c from TTL c left join fetch c.safeZoneList where c.id=?1")
    Optional<TTL> findByFetchSafeZone(Integer id);
}
