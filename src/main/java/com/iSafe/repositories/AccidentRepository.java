package com.iSafe.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.Accident;

public interface AccidentRepository extends CrudRepository<Accident, Long> {
	
	@Query("select a from Accident a where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(lat)) * cos(radians(lng) - radians(?2)) + sin (radians(?1)) * sin(radians(lat)))) <= 0.5")
	public List<Accident> findByLatLan(double lat, double lng) ;
	
	@Query("select a from Accident a where isConfirmed=1")
	public List<Accident> findAllByLatLan();
	
	@Query("select i from Accident i where i.isConfirmed=0 and i.lat=?1 and i.lng=?2")
	public Accident findByLatLanNotConfirmed(double lat, double lng);
	
	@Query("select i from Accident i where i.isConfirmed=0")
	public List<Accident> findAllNotConfirmed();
	
	@Modifying
	@Transactional
	@Query("update Accident set isConfirmed=1, adminId=?3 where lat=?1 and lng=?2")
	public Accident confirmRecord(double lat, double lng, String kid);
	
	@Modifying
	@Transactional
	@Query("delete from Accident where isConfirmed=0 and lat=?1 and lng=?2")
	public void deleteRecord(double lat, double lng);
	
	@Query("select a from Accident a where isConfirmed=1 and (6371 * acos (cos ( radians(?1) ) * cos(radians(lat)) * cos(radians(lng) - radians(?2)) + sin (radians(?1)) * sin(radians(lat)))) <= ?3")
	public List<Accident> findByRadius(double lat, double lng, double radius);
}
