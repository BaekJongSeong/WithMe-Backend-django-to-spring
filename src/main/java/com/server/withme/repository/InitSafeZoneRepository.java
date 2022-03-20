package com.server.withme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.withme.entity.InitSafeZone;

public interface InitSafeZoneRepository extends JpaRepository<InitSafeZone, Integer>{
	
	@Query("select c from AccountOption c left join fetch c.initSafeZoneList where c.id=?1")
    List<InitSafeZone> findByJoinFetch(Integer id);
}
