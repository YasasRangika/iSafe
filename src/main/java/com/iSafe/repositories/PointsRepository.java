package com.iSafe.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.Point;

public interface PointsRepository extends CrudRepository<Point, Long> {

	@Query("select p from Point p where userId=?1")
	public Point getPointsOfUser(String kid);
	
	@Modifying
	@Transactional
	@Query("update Point set points=?2 where userId=?1")
	public boolean addPointsToUser(String kid, int points);
}
