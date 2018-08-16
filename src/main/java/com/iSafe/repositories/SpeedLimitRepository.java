package com.iSafe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.SpeedLimit;

public interface SpeedLimitRepository extends CrudRepository<SpeedLimit, Long> {
	
	@Query("select s from SpeedLimit s where (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= 0.001")
	public SpeedLimit findSpeedLimitPoint(double latitude, double longitude);
	
	@Query("select s, (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) from SpeedLimit s where (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= ?3")
	public List<SpeedLimit> findByRadius(double lat, double lng, double radius);
	
}
