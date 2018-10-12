package com.iSafe.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.SpeedLimit;

//These are the queries to do CRUD activities using java.

public interface SpeedLimitRepository extends CrudRepository<SpeedLimit, Long> {
	
	@Query("select s from SpeedLimit s")
	public List<SpeedLimit> findAllSpeedLimitedPoints();
	
	//Retrieve data within a radius
	@Query("select s from SpeedLimit s where (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= 0.01")
	public List<SpeedLimit> findSpeedLimitPoint(double latitude, double longitude);
	
	@Query("select s from SpeedLimit s where (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= ?3")
	public List<SpeedLimit> findByRadius(double lat, double lng, double radius);
	
	@Modifying
	@Transactional
	@Query("delete from SpeedLimit where latitude=?1 and longitude=?2 and speedLimit=?3")
	public int deleteRecord(double lat, double lng, double speedLimit);
	
}
