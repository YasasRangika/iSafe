package com.iSafe.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.CriticalPoint;

//These are the queries to do CRUD activities using java.

public interface CriticalPointRepository extends CrudRepository<CriticalPoint, Long> {

	//Retrieve data within a radius
	@Query("select c from CriticalPoint c where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= 0.01")
	public List<CriticalPoint> findCriticalPoint(double latitude, double longitude);

	@Query("select c from CriticalPoint c where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= ?3")
	public List<CriticalPoint> findByRadius(double lat, double lng, double radius);

	@Query("select c from CriticalPoint c")
	public List<CriticalPoint> findAllCriticalPoints();
	
	@Modifying
	@Transactional
	@Query("delete from CriticalPoint where latitude=?1 and longitude=?2 and message=?3")
	public int deleteRecord(double lat, double lng, String message);
}
