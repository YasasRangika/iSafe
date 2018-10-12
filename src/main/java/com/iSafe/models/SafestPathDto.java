package com.iSafe.models;

import java.util.List;

//Structure of transaction of data in safest path calculation

public class SafestPathDto {
	
	private List<RecordDto> points;
	private int blackSpots;
	private int criticalPoints;
	private int totalRisks;
	
	public int getBlackSpots() {
		return blackSpots;
	}
	
	public void setBlackSpots(int blackSpots) {
		this.blackSpots = blackSpots;
	}

	public List<RecordDto> getPoints() {
		return points;
	}

	public void setPoints(List<RecordDto> points) {
		this.points = points;
	}

	public int getCriticalPoints() {
		return criticalPoints;
	}

	public void setCriticalPoints(int criticalPoints) {
		this.criticalPoints = criticalPoints;
	}

	public int getTotalRisks() {
		return totalRisks;
	}

	public void setTotalRisks(int totalRisks) {
		this.totalRisks = totalRisks;
	}

	
}
