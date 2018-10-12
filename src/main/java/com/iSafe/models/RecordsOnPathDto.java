package com.iSafe.models;

import java.util.List;

//import com.iSafe.entities.Accident;
import com.iSafe.entities.BlackSpot;
import com.iSafe.entities.CriticalPoint;
import com.iSafe.entities.RoadSigns;
import com.iSafe.entities.SpeedLimit;

//This is the JSON format of all kind of record data that transfer within the server and the front-end
//This format use to identify what is the structure of received data

public class RecordsOnPathDto {

	//private List<Accident> accidents;
	private List<BlackSpot> blackSpots;
	private List<CriticalPoint> criticalPoints;
	private List<RoadSigns> roadSigns;
	private List<SpeedLimit> speedLimits;
	
//	public List<Accident> getAccidents() {
//		return accidents;
//	}
//	public void setAccidents(List<Accident> accidents) {
//		this.accidents = accidents;
//	}
	public List<BlackSpot> getBlackSpots() {
		return blackSpots;
	}
	public void setBlackSpots(List<BlackSpot> blackSpots) {
		this.blackSpots = blackSpots;
	}
	public List<CriticalPoint> getCriticalPoints() {
		return criticalPoints;
	}
	public void setCriticalPoints(List<CriticalPoint> criticalPoints) {
		this.criticalPoints = criticalPoints;
	}
	public List<RoadSigns> getRoadSigns() {
		return roadSigns;
	}
	public void setRoadSigns(List<RoadSigns> roadSigns) {
		this.roadSigns = roadSigns;
	}
	public List<SpeedLimit> getSpeedLimits() {
		return speedLimits;
	}
	public void setSpeedLimits(List<SpeedLimit> speedLimits) {
		this.speedLimits = speedLimits;
	}
	
	
}
