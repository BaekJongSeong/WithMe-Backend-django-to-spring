package com.server.withme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.withme.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{

    Location findByAccountOption_Id(Integer id);
}
