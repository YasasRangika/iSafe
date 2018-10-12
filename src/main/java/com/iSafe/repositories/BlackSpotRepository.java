package com.iSafe.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.BlackSpot;

//These are the queries to do CRUD activities using java.

public interface BlackSpotRepository extends CrudRepository<BlackSpot, Long> {
	
	//Retrieve data within a radius
	@Query("select b from BlackSpot b where (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= 0.5")
	public List<BlackSpot> findBlackSpot(double latitude, double longitude);
	
	@Query("select b from BlackSpot b where (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= ?3")
	public List<BlackSpot> findByRadius(double lat, double lng, double radius);
	
	@Query("select b from BlackSpot b")
	public List<BlackSpot> findAllBlackSpots();
	
	@Modifying
	@Transactional
	@Query("delete from BlackSpot where latitude=?1 and longitude=?2 and message=?3")
	public int deleteRecord(double lat, double lng, String message);
	
//	@Query("select b from BlackSpot b where b.blackSpotId=?1")
//	public BlackSpot findByBlackSpotId(String id);
}
