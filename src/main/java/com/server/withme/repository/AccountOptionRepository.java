package com.server.withme.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.server.withme.entity.AccountOption;

public interface AccountOptionRepository extends JpaRepository<AccountOption, Integer>{
	
	@Query("select c from AccountOption c left join fetch c.initSafeZoneList where c.id=?1")
    Optional<AccountOption> findByFetchInitSafeZone(Integer id);
	
	@Query("select c from AccountOption c left join fetch c.locationList where c.id=?1")
    Optional<AccountOption> findByFetchLocation(Integer id);
	
	@Query("select c from AccountOption c left join fetch c.ttlList where c.id=?1")
    Optional<AccountOption> findByFetchTTL(Integer id);
	
	@Modifying
	@Query("update AccountOption c set c.initSafeZone=true, c.xPoint= :latitude, c.yPoint= :longitude where c.id= :id")
	void updateQuery(@Param("latitude") Double latitude,@Param("longitude") Double longitude,@Param("id") Integer id);
}
