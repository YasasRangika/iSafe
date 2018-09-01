package com.iSafe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.BlackSpot;

public interface BlackSpotRepository extends CrudRepository<BlackSpot, Long> {
	
	@Query("select b from BlackSpot b where (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= 0.5")
	public BlackSpot findBlackSpot(double latitude, double longitude);
	
	@Query("select b from BlackSpot b where (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= ?3")
	public List<BlackSpot> findByRadius(double lat, double lng, double radius);
	
//	@Query("select b from BlackSpot b where b.blackSpotId=?1")
//	public BlackSpot findByBlackSpotId(String id);
}
