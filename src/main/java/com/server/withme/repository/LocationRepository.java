package com.server.withme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.withme.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{

	@Query("select c from location c where location.id = (SELECT max(d.id) FROM location d WHERE d.accountOptionId=?1)")
    Location findByFetchLatest(Integer id);
}
