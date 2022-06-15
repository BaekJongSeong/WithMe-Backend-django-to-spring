package com.server.withme.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.withme.entity.SafeZone;

public interface SafeZoneRepository extends JpaRepository<SafeZone, Integer> {
	
	public void saveAll(List<SafeZone> items);
}
