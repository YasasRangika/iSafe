package com.iSafe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.CriticalPoint;

public interface CriticalPointRepository extends CrudRepository<CriticalPoint, Long> {

	@Query("select c from CriticalPoint c where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= 0.01")
	public CriticalPoint findCriticalPoint(double latitude, double longitude);

	@Query("select c from CriticalPoint c where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= ?3")
	public List<CriticalPoint> findByRadius(double lat, double lng, double radius);

//	@Query("select c from CriticalPoint c where criticalPointId=?1")
//	public CriticalPoint findByCriticalPointId(String id);
}
