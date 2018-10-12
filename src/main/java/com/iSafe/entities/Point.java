package com.iSafe.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//This is the table creation of points of users table
//User id will take from user table

@Entity
public class Point {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String userId;
	private int points;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	

}
