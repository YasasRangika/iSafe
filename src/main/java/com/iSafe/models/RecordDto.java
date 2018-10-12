package com.iSafe.models;

import com.iSafe.entities.BlackSpot;
import com.iSafe.entities.CriticalPoint;

import java.sql.Date;

import com.iSafe.entities.Accident;
import com.iSafe.entities.RoadSigns;
import com.iSafe.entities.SpeedLimit;

//This is the format of record data that transfer within the server and the front-end
//This format use to identify what is the structure of received data
//There is no need to fill all the variables include here
//According to the situation set variables and send them to the server

public class RecordDto {

	private String self;
	private double latitude;
    private double longitude;
    private double radius;
    private String message;
    private int con;
    
    private String startTime;
    private String endTime;
    
	private Date date;
	private String reporter;
	private String accidentDesc;
	private String accidentType;
	private String photoUrl;
	
	private RoadSigns roadsigns;
	private Accident incident;
	private BlackSpot blackSpot;
	private CriticalPoint criticalPoint;
	private SpeedLimit speedLimit;
	
    private String sign;
	
    private double limit;
    private double thresholdLimit;
    
    private int isConfirmed;
    private String reporterId;

    
	public String getReporterId() {
		return reporterId;
	}

	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}
	
	public int getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(int isConfirmed) {
		this.isConfirmed = isConfirmed;
	}
	
	public String getSelf() {
		return self;
	}
	public void setSelf(String self) {
		this.self = self;
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
	public int getCon() {
		return con;
	}
	public void setCon(int con) {
		this.con = con;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public String getAccidentDesc() {
		return accidentDesc;
	}
	public void setAccidentDesc(String accidentDesc) {
		this.accidentDesc = accidentDesc;
	}
	public String getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}
	public RoadSigns getRoadsigns() {
		return roadsigns;
	}
	public void setRoadsigns(RoadSigns roadsigns) {
		this.roadsigns = roadsigns;
	}
	public Accident getIncident() {
		return incident;
	}
	public void setIncident(Accident incident) {
		this.incident = incident;
	}
	public BlackSpot getBlackSpot() {
		return blackSpot;
	}
	public void setBlackSpot(BlackSpot blackSpot) {
		this.blackSpot = blackSpot;
	}
	public CriticalPoint getCriticalPoint() {
		return criticalPoint;
	}
	public void setCriticalPoint(CriticalPoint criticalPoint) {
		this.criticalPoint = criticalPoint;
	}
	public SpeedLimit getSpeedLimit() {
		return speedLimit;
	}
	public void setSpeedLimit(SpeedLimit speedLimit) {
		this.speedLimit = speedLimit;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public double getLimit() {
		return limit;
	}
	public void setLimit(double limit) {
		this.limit = limit;
	}
	public double getThresholdLimit() {
		return thresholdLimit;
	}
	public void setThresholdLimit(double thresholdLimit) {
		this.thresholdLimit = thresholdLimit;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
    
    
}
