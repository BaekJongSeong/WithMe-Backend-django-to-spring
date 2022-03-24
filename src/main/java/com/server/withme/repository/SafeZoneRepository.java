package com.server.withme.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.server.withme.entity.SafeZone;

public interface SafeZoneRepository extends JpaRepository<SafeZone, Integer> {
	
}
