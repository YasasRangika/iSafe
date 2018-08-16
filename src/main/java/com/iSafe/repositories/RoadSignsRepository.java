package com.iSafe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.RoadSigns;

public interface RoadSignsRepository extends CrudRepository<RoadSigns, Long> {
	
	@Query("select r from RoadSigns r where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= 0.001")
	public RoadSigns findByLatLan(double latitude, double longitude);
	
	@Query("select r from RoadSigns r where r.latitude=?1 and r.longitude=?2")
	public RoadSigns findByLatLanNotConfirmed(double lat, double lng);
	
	@Query("update RoadSigns set isConfirmed=1 where latitude=?1 and longitude=?2")
	public RoadSigns confirmRecord(double lat, double lng);
	
	@Query("delete from RoadSigns where latitude=?1 and longitude=?2")
	public void deleteRecord(double lat, double lng);
	
	@Query("select r, (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) from RoadSigns r where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= ?3")
	public List<RoadSigns> findByRadius(double lat, double lng, double radius);
}
