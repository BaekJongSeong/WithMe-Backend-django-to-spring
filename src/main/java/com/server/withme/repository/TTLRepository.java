package com.server.withme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.withme.entity.TTL;

public interface TTLRepository extends JpaRepository<TTL, Integer>{
	
	@Query("select c from AccountOption c left join fetch c.ttlList where c.id=?1")
    List<TTL> findJoinFetch(Integer id);
}
