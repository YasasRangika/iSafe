package com.iSafe.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//This is the table creation of speed limits

@Entity
public class SpeedLimit {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private double latitude;
    private double longitude;
    private double speedLimit;
    private double thresholdLimit;
    private double radius;
    private String message;
//    private String reporterId;
//
//	public String getReporterId() {
//		return reporterId;
//	}
//
//	public void setReporterId(String reporterId) {
//		this.reporterId = reporterId;
//	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
	}

	public double getThresholdLimit() {
		return thresholdLimit;
	}

	public void setThresholdLimit(double thresholdLimit) {
		this.thresholdLimit = thresholdLimit;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
