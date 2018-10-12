package com.iSafe.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.RoadSigns;

//These are the queries to do CRUD activities using java.

public interface RoadSignsRepository extends CrudRepository<RoadSigns, Long> {
	
	@Query("select r from RoadSigns r where isConfirmed=1")
	public List<RoadSigns> findAllSigns();
	
	//Retrieve data within a radius
	@Query("select r from RoadSigns r where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= 0.01")
	public List<RoadSigns> findByLatLan(double latitude, double longitude);
	
	@Query("select r from RoadSigns r where r.isConfirmed=0 and r.latitude=?1 and r.longitude=?2")
	public RoadSigns findByLatLanNotConfirmed(double lat, double lng);
	
	@Modifying
	@Transactional
	@Query("update RoadSigns set isConfirmed=1, adminId=?3 where latitude=?1 and longitude=?2")
	public void confirmRecord(double lat, double lng, String kid);
	
	@Modifying
	@Transactional
	@Query("delete from RoadSigns where isConfirmed=0 and latitude=?1 and longitude=?2")
	public void deleteRecord(double lat, double lng);
	
	@Modifying
	@Transactional
	@Query("delete from RoadSigns where latitude=?1 and longitude=?2 and sign=?3")
	public int deleteRecord(double lat, double lng, String sign);
	
	@Query("select r from RoadSigns r where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude)))) <= ?3") //, (6371 * acos (cos ( radians(?1) ) * cos(radians(latitude)) * cos(radians(longitude) - radians(?2)) + sin (radians(?1)) * sin(radians(latitude))))
	public List<RoadSigns> findByRadius(double lat, double lng, double radius);
}
