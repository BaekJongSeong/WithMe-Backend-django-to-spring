package com.server.withme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.withme.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{

	@Query("select c from AccountOption c left join fetch c.locationList where c.id=?1 and location.id = (SELECT max(id) FROM location)")
    Location findByJoinFetchLatest(Integer id);
	
	@Query("select c from AccountOption c left join fetch c.locationList where c.id=?1")
    List<Location> findByJoinFetch(Integer id);
}
