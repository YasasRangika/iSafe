package com.iSafe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.Accident;

public interface AccidentRepository extends CrudRepository<Accident, Long> {
	
	@Query("select a from Accident a where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(lat)) * cos(radians(lng) - radians(?2)) + sin (radians(?1)) * sin(radians(lat)))) <= 0.001")
	public Accident findByLatLan(double lat, double lng) ;
	
	@Query("select i from Accident i where i.lat=?1 and i.lng=?2")
	public Accident findByLatLanNotConfirmed(double lat, double lng);
	
	@Query("update Accident set isConfirmed=1, adminId=?1")
	public Accident confirmRecord();
	
	@Query("delete from Accident where lat=?1 and lng=?2")
	public void deleteRecord(double lat, double lng);
	
	@Query("select a, (6371 * acos (cos ( radians(?1) ) * cos(radians(lat)) * cos(radians(lng) - radians(?2)) + sin (radians(?1)) * sin(radians(lat)))) from Accident a where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(lat)) * cos(radians(lng) - radians(?2)) + sin (radians(?1)) * sin(radians(lat)))) <= ?3")
	public List<Accident> findByRadius(double lat, double lng, double radius);
}
