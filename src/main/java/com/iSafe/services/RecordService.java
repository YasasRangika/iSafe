package com.iSafe.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iSafe.entities.Accident;
import com.iSafe.entities.BlackSpot;
import com.iSafe.entities.CriticalPoint;
import com.iSafe.entities.Point;
import com.iSafe.entities.RoadSigns;
import com.iSafe.entities.SpeedLimit;
import com.iSafe.models.RecordDto;
import com.iSafe.models.RecordsOnPathDto;
import com.iSafe.models.SafestPathDto;
import com.iSafe.models.UserDTO;
import com.iSafe.repositories.AccidentRepository;
import com.iSafe.repositories.BlackSpotRepository;
import com.iSafe.repositories.CriticalPointRepository;
import com.iSafe.repositories.PointsRepository;
import com.iSafe.repositories.RoadSignsRepository;
import com.iSafe.repositories.SpeedLimitRepository;

//Handle all the record activities

@Service
public class RecordService {

	@Autowired
	CriticalPointRepository criticalPointRepository;
	@Autowired
	AccidentRepository accidentRepository;
	@Autowired
	private RoadSignsRepository roadSignsRepository;
	@Autowired
	private BlackSpotRepository blackSpotRepository;
	@Autowired
	private SpeedLimitRepository speedLimitRepository;
	@Autowired
	private PointsRepository pointsRepository;

	//Safest path finding algorithm
	
	public List<SafestPathDto> findSafestPath(List<List<RecordDto>> points) {

		List<SafestPathDto> safePath = new ArrayList<>();

		for (List<RecordDto> record : points) {
			RecordsOnPathDto recordsOnPathDto = this.pointsOnRoad(record);
			SafestPathDto safestPathDto = new SafestPathDto();
			safestPathDto.setBlackSpots(recordsOnPathDto.getBlackSpots().size());
			safestPathDto.setCriticalPoints(recordsOnPathDto.getCriticalPoints().size());
			safestPathDto.setPoints(record);
			safestPathDto.setTotalRisks(
					recordsOnPathDto.getBlackSpots().size() + recordsOnPathDto.getCriticalPoints().size());
			safePath.add(safestPathDto);
		}
		return safePath;
	}
	
	//Retrieve all the static data records in databases when front-end send the all the path point data
	//Except Accidents
	//Authentication is essential

	public RecordsOnPathDto pointsOnRoad(List<RecordDto> recordDtos) {

		RecordsOnPathDto recordsOnPathDto = new RecordsOnPathDto();

//		List<Accident> acc = new ArrayList<>();
		List<BlackSpot> blc = new ArrayList<>();
		List<CriticalPoint> crtcl = new ArrayList<>();
		List<RoadSigns> rdsign = new ArrayList<>();
		List<SpeedLimit> spd = new ArrayList<>();

		for (RecordDto recordDto : recordDtos) {
//			if (accidentRepository.findByLatLan(recordDto.getLatitude(), recordDto.getLongitude()) != null)
//				acc.add(accidentRepository.findByLatLan(recordDto.getLatitude(), recordDto.getLongitude()));
			if (blackSpotRepository.findBlackSpot(recordDto.getLatitude(), recordDto.getLongitude()).size() != 0) {
				List<BlackSpot> blckspts = blackSpotRepository.findBlackSpot(recordDto.getLatitude(),
						recordDto.getLongitude());
				for (BlackSpot b : blckspts) {
					if (!blc.contains(b))
						blc.add(b);
				}
			}

			if (criticalPointRepository.findCriticalPoint(recordDto.getLatitude(), recordDto.getLongitude())
					.size() != 0) {
				List<CriticalPoint> crtclpoint = criticalPointRepository.findCriticalPoint(recordDto.getLatitude(),
						recordDto.getLongitude());
				for (CriticalPoint c : crtclpoint) {
					if (!crtcl.contains(c))
						crtcl.add(c);
				}
			}

			if (roadSignsRepository.findByLatLan(recordDto.getLatitude(), recordDto.getLongitude()).size() != 0) {
				List<RoadSigns> rdsigns = roadSignsRepository.findByLatLan(recordDto.getLatitude(),
						recordDto.getLongitude());
				for (RoadSigns r : rdsigns) {
					if (!rdsign.contains(r))
						rdsign.add(r);
				}
			}

			if (speedLimitRepository.findSpeedLimitPoint(recordDto.getLatitude(), recordDto.getLongitude())
					.size() != 0) {
				List<SpeedLimit> spdlmt = speedLimitRepository.findSpeedLimitPoint(recordDto.getLatitude(),
						recordDto.getLongitude());
				for (SpeedLimit s : spdlmt) {
					if (!spd.contains(s))
						spd.add(s);
				}
			}

		}
//		if (acc != null)
//			recordsOnPathDto.setAccidents(acc);
		if (blc.size() != 0)
			recordsOnPathDto.setBlackSpots(blc);
		if (crtcl.size() != 0)
			recordsOnPathDto.setCriticalPoints(crtcl);
		if (rdsign.size() != 0)
			recordsOnPathDto.setRoadSigns(rdsign);
		if (spd.size() != 0)
			recordsOnPathDto.setSpeedLimits(spd);

		return recordsOnPathDto;
	}

	//Above same activity to be done without authentication in admin controller class
	
	public RecordsOnPathDto allRecords() {

		RecordsOnPathDto recordsOnPathDto = new RecordsOnPathDto();

		List<BlackSpot> blc = new ArrayList<>();
		List<CriticalPoint> crtcl = new ArrayList<>();
		List<RoadSigns> rdsign = new ArrayList<>();
		List<SpeedLimit> spd = new ArrayList<>();

		if (blackSpotRepository.findAllBlackSpots().size() != 0) {
			List<BlackSpot> blckspts = blackSpotRepository.findAllBlackSpots();
			for (BlackSpot b : blckspts) {
				if (!blc.contains(b))
					blc.add(b);
			}
		}

		if (criticalPointRepository.findAllCriticalPoints().size() != 0) {
			List<CriticalPoint> crtclpoint = criticalPointRepository.findAllCriticalPoints();
			for (CriticalPoint c : crtclpoint) {
				if (!crtcl.contains(c))
					crtcl.add(c);
			}
		}

		if (roadSignsRepository.findAllSigns().size() != 0) {
			List<RoadSigns> rdsigns = roadSignsRepository.findAllSigns();
			for (RoadSigns r : rdsigns) {
				if (!rdsign.contains(r))
					rdsign.add(r);
			}
		}

		if (speedLimitRepository.findAllSpeedLimitedPoints().size() != 0) {
			List<SpeedLimit> spdlmt = speedLimitRepository.findAllSpeedLimitedPoints();
			for (SpeedLimit s : spdlmt) {
				if (!spd.contains(s))
					spd.add(s);
			}
		}

		if (blc.size() != 0)
			recordsOnPathDto.setBlackSpots(blc);
		if (crtcl.size() != 0)
			recordsOnPathDto.setCriticalPoints(crtcl);
		if (rdsign.size() != 0)
			recordsOnPathDto.setRoadSigns(rdsign);
		if (spd.size() != 0)
			recordsOnPathDto.setSpeedLimits(spd);

		return recordsOnPathDto;
	}

	//Method to remove record from data base
	
	public int removeRecord(RecordDto recordDto) {
		int status;
		if (roadSignsRepository.deleteRecord(recordDto.getLatitude(), recordDto.getLongitude(),
				recordDto.getSign()) > 0) {
			status = 201;
		} else if (blackSpotRepository.deleteRecord(recordDto.getLatitude(), recordDto.getLongitude(),
				recordDto.getMessage()) > 0) {
			status = 202;
		} else if (criticalPointRepository.deleteRecord(recordDto.getLatitude(), recordDto.getLongitude(),
				recordDto.getMessage()) > 0) {
			status = 203;
		} else if (speedLimitRepository.deleteRecord(recordDto.getLatitude(), recordDto.getLongitude(),
				recordDto.getLimit()) > 0) {
			status = 204;
		} else {

			status = 400;
		}
		return status;
	}

	//Confirm or delete road signs
	//Here is place that gives points to the user
	
	public int confirmOrDeleteAccident(RecordDto recordDto, UserDTO userDTO) {
		if (accidentRepository.findByLatLanNotConfirmed(recordDto.getLatitude(), recordDto.getLongitude()) != null) {
			Point p = pointsRepository.getPointsOfUser(userDTO.getKid());
			if (recordDto.getIsConfirmed() == 1) {
				accidentRepository.confirmRecord(recordDto.getLatitude(), recordDto.getLongitude(), userDTO.getKid());
				if (recordDto.getPhotoUrl() != null) {
					pointsRepository.addPointsToUser(userDTO.getKid(), (p.getPoints() + 50));
				}
				pointsRepository.addPointsToUser(userDTO.getKid(), (p.getPoints() + 25));/// may be this will come
																							/// directly with userID
																							/// because record recording
																							/// with userID who recorded
				return 200;
			} else {
				accidentRepository.deleteRecord(recordDto.getLatitude(), recordDto.getLongitude());
				pointsRepository.addPointsToUser(userDTO.getKid(), (p.getPoints() - 5));
				return 201;
			}
		} else {
			return 208;
		}
	}

	//From here all the methods and their tasks are same as the name implies
	
	public int confirmOrDeleteRoadSign(RecordDto recordDto, UserDTO userDTO) {
		if (roadSignsRepository.findByLatLanNotConfirmed(recordDto.getLatitude(), recordDto.getLongitude()) != null) {

			if (recordDto.getIsConfirmed() == 1) {
				roadSignsRepository.confirmRecord(recordDto.getLatitude(), recordDto.getLongitude(), userDTO.getKid());
				return 200;
			} else {
				roadSignsRepository.deleteRecord(recordDto.getLatitude(), recordDto.getLongitude());
				return 201;
			}
		} else {
			return 208;
		}
	}

	public RecordDto addNewCriticalPoint(RecordDto criticalPointDto, UserDTO userDTO) {

		RecordDto rtnCriticalPointDto = new RecordDto();
		CriticalPoint criticalPoint = new CriticalPoint();

		if (criticalPointRepository.findCriticalPoint(criticalPointDto.getLatitude(), criticalPointDto.getLongitude())
				.size() == 0) {

			criticalPoint.setLatitude(criticalPointDto.getLatitude());
			criticalPoint.setLongitude(criticalPointDto.getLongitude());
			criticalPoint.setRadius(criticalPointDto.getRadius());
			criticalPoint.setMessage(criticalPointDto.getMessage());
			criticalPoint.setStartTime(criticalPointDto.getStartTime());
			criticalPoint.setEndTime(criticalPointDto.getEndTime());
			criticalPoint.setIsConfirmed(criticalPointDto.getIsConfirmed());
			criticalPoint.setReporterId(userDTO.getKid());

			rtnCriticalPointDto.setLatitude(criticalPoint.getLatitude());
			rtnCriticalPointDto.setLongitude(criticalPoint.getLongitude());
			rtnCriticalPointDto.setRadius(criticalPoint.getRadius());
			rtnCriticalPointDto.setMessage(criticalPoint.getMessage());
			rtnCriticalPointDto.setStartTime(criticalPoint.getStartTime());
			rtnCriticalPointDto.setEndTime(criticalPoint.getEndTime());
			rtnCriticalPointDto.setIsConfirmed(criticalPoint.getIsConfirmed());
			rtnCriticalPointDto.setReporterId(criticalPoint.getReporterId());

			criticalPointRepository.save(criticalPoint);
			rtnCriticalPointDto.setSelf("http://localhost:8081/team8/newCritical" + criticalPoint.getId());
			return rtnCriticalPointDto;
		} else {
			rtnCriticalPointDto.setSelf("Exists");
			return rtnCriticalPointDto;
		}
	}

	public RecordDto addNewCriticalPoint2(RecordDto criticalPointDto) {

		RecordDto rtnCriticalPointDto = new RecordDto();
		CriticalPoint criticalPoint = new CriticalPoint();

		if (criticalPointRepository.findCriticalPoint(criticalPointDto.getLatitude(), criticalPointDto.getLongitude())
				.size() == 0) {

			criticalPoint.setLatitude(criticalPointDto.getLatitude());
			criticalPoint.setLongitude(criticalPointDto.getLongitude());
			criticalPoint.setRadius(criticalPointDto.getRadius());
			criticalPoint.setMessage(criticalPointDto.getMessage());
			criticalPoint.setStartTime(criticalPointDto.getStartTime());
			criticalPoint.setEndTime(criticalPointDto.getEndTime());
			criticalPoint.setIsConfirmed(criticalPointDto.getIsConfirmed());

			rtnCriticalPointDto.setLatitude(criticalPoint.getLatitude());
			rtnCriticalPointDto.setLongitude(criticalPoint.getLongitude());
			rtnCriticalPointDto.setRadius(criticalPoint.getRadius());
			rtnCriticalPointDto.setMessage(criticalPoint.getMessage());
			rtnCriticalPointDto.setStartTime(criticalPoint.getStartTime());
			rtnCriticalPointDto.setEndTime(criticalPoint.getEndTime());
			rtnCriticalPointDto.setIsConfirmed(criticalPoint.getIsConfirmed());

			criticalPointRepository.save(criticalPoint);
			rtnCriticalPointDto.setSelf("http://localhost:8081/team8/newCritical" + criticalPoint.getId());
			return rtnCriticalPointDto;
		} else {
			rtnCriticalPointDto.setSelf("Exists");
			return rtnCriticalPointDto;
		}
	}

	public List<CriticalPoint> getCriticalPoint(RecordDto criticalPointDto) {

		List<CriticalPoint> criticalPoint = new ArrayList<CriticalPoint>();

		if (criticalPointRepository.findByRadius(criticalPointDto.getLatitude(), criticalPointDto.getLongitude(),
				criticalPointDto.getRadius()).size() != 0) {

			criticalPoint = criticalPointRepository.findByRadius(criticalPointDto.getLatitude(),
					criticalPointDto.getLongitude(), criticalPointDto.getRadius());

			return criticalPoint;
		} else {
			return criticalPoint;
		}
	}

	public List<CriticalPoint> getCriticalPoints() {

		List<CriticalPoint> criticalPoint = new ArrayList<CriticalPoint>();

		if (criticalPointRepository.findAllCriticalPoints().size() != 0) {

			criticalPoint = criticalPointRepository.findAllCriticalPoints();

			return criticalPoint;
		} else {
			return criticalPoint;
		}
	}

	public RecordDto addIncident(RecordDto incidentDto, UserDTO userDTO) {

		RecordDto returnIncidentDto = new RecordDto();
		Accident incident = new Accident();
//		System.out.println("Point 01");

		if (accidentRepository.findByLatLan1(incidentDto.getLatitude(), incidentDto.getLongitude()).size() == 0) {

			incident.setLat(incidentDto.getLatitude());
			incident.setLng(incidentDto.getLongitude());
			incident.setAccident(incidentDto.getAccidentDesc());
			incident.setAccidentType(incidentDto.getAccidentType());
			incident.setPhotoUrl(incidentDto.getPhotoUrl());
			incident.setDate(incidentDto.getDate());
			incident.setReporter(incidentDto.getReporter());
			incident.setIsConfirmed(incidentDto.getIsConfirmed());
			incident.setReporterId(userDTO.getKid());

			returnIncidentDto.setLatitude(incident.getLat());
			returnIncidentDto.setLongitude(incident.getLng());
			returnIncidentDto.setAccidentDesc(incident.getAccident());
			returnIncidentDto.setAccidentType(incident.getAccidentType());
			returnIncidentDto.setPhotoUrl(incident.getPhotoUrl());
			returnIncidentDto.setDate(incident.getDate());
			returnIncidentDto.setReporter(incident.getReporter());
			returnIncidentDto.setIsConfirmed(incident.getIsConfirmed());
			returnIncidentDto.setReporterId(incident.getReporterId());

			accidentRepository.save(incident);
			returnIncidentDto.setSelf("http://localhost:8081/team8/incident" + incident.getId());
			return returnIncidentDto;
		} else {
			returnIncidentDto.setSelf("Exists");
			return returnIncidentDto;
		}
	}

	public RecordDto addIncident1(RecordDto incidentDto) {

		RecordDto returnIncidentDto = new RecordDto();
		Accident incident = new Accident();
//		System.out.println("Point 01");

		if (accidentRepository.findByLatLan1(incidentDto.getLatitude(), incidentDto.getLongitude()).size() == 0) {

			incident.setLat(incidentDto.getLatitude());
			incident.setLng(incidentDto.getLongitude());
			incident.setAccident(incidentDto.getAccidentDesc());
			incident.setAccidentType(incidentDto.getAccidentType());
			incident.setPhotoUrl(incidentDto.getPhotoUrl());
			incident.setDate(incidentDto.getDate());
			incident.setReporter(incidentDto.getReporter());
			incident.setIsConfirmed(incidentDto.getIsConfirmed());

			returnIncidentDto.setLatitude(incident.getLat());
			returnIncidentDto.setLongitude(incident.getLng());
			returnIncidentDto.setAccidentDesc(incident.getAccident());
			returnIncidentDto.setAccidentType(incident.getAccidentType());
			returnIncidentDto.setPhotoUrl(incident.getPhotoUrl());
			returnIncidentDto.setDate(incident.getDate());
			returnIncidentDto.setReporter(incident.getReporter());
			returnIncidentDto.setIsConfirmed(incident.getIsConfirmed());

			accidentRepository.save(incident);
			returnIncidentDto.setSelf("http://localhost:8081/team8/incident" + incident.getId());
			return returnIncidentDto;
		} else {
			returnIncidentDto.setSelf("Exists");
			return returnIncidentDto;
		}
	}

	public boolean recordBlackSpot(RecordDto incidentDto) {

		BlackSpot blackSpot = new BlackSpot();

		blackSpot.setLatitude(incidentDto.getLatitude());
		blackSpot.setLongitude(incidentDto.getLongitude());
		blackSpot.setRadius(incidentDto.getRadius());
		blackSpot.setMessage(incidentDto.getMessage());

		if (blackSpotRepository.findBlackSpot(blackSpot.getLatitude(), blackSpot.getLongitude()).size() == 0) {
			blackSpotRepository.save(blackSpot);
			return true;
		} else {
			return false;
		}
	}

	public List<Accident> getIncident(RecordDto incidentDto) {

		List<Accident> incident = new ArrayList<Accident>();

		if (accidentRepository
				.findByRadius(incidentDto.getLatitude(), incidentDto.getLongitude(), incidentDto.getRadius())
				.size() != 0) {
			incident = accidentRepository.findByRadius(incidentDto.getLatitude(), incidentDto.getLongitude(),
					incidentDto.getRadius());

			return incident;
		} else {
			return incident;
		}
	}

	public List<Accident> getIncidents() {

		List<Accident> incident = new ArrayList<Accident>();

		if (accidentRepository.findAllByLatLan().size() != 0) {
			incident = accidentRepository.findAllByLatLan();

			return incident;
		} else {
			return incident;
		}
	}

	public List<BlackSpot> getBlackSpot(RecordDto blackSpotDto) {

		List<BlackSpot> blackSpot = new ArrayList<BlackSpot>();

		if (blackSpotRepository
				.findByRadius(blackSpotDto.getLatitude(), blackSpotDto.getLongitude(), blackSpotDto.getRadius())
				.size() != 0) {

			blackSpot = blackSpotRepository.findByRadius(blackSpotDto.getLatitude(), blackSpotDto.getLongitude(),
					blackSpotDto.getRadius());

			return blackSpot;
		} else {
			return blackSpot;
		}
	}

	public List<BlackSpot> getBlackSpots() {

		List<BlackSpot> blackSpot = new ArrayList<BlackSpot>();

		if (blackSpotRepository.findAllBlackSpots().size() != 0) {

			blackSpot = blackSpotRepository.findAllBlackSpots();

			return blackSpot;
		} else {
			return blackSpot;
		}
	}

	public RecordDto saveNewSign(RecordDto roadSignsDto, UserDTO userDTO) {

		RecordDto returnRoadSignsDto = new RecordDto();
		RoadSigns roadSigns = new RoadSigns();

		if (roadSignsRepository.findByLatLan(roadSignsDto.getLatitude(), roadSignsDto.getLongitude()).size() == 0) {

			roadSigns.setLatitude(roadSignsDto.getLatitude());
			roadSigns.setLongitude(roadSignsDto.getLongitude());
			roadSigns.setSign(roadSignsDto.getSign());
			roadSigns.setMessage(roadSignsDto.getMessage());
			roadSigns.setIsConfirmed(roadSignsDto.getIsConfirmed());
			roadSigns.setReporterId(userDTO.getKid());

			returnRoadSignsDto.setLatitude(roadSigns.getLatitude());
			returnRoadSignsDto.setLongitude(roadSigns.getLongitude());
			returnRoadSignsDto.setSign(roadSigns.getSign());
			returnRoadSignsDto.setMessage(roadSigns.getMessage());
			returnRoadSignsDto.setIsConfirmed(roadSigns.getIsConfirmed());
			returnRoadSignsDto.setReporterId(roadSigns.getReporterId());

			returnRoadSignsDto.setSelf("http://localhost:8081/team8/sign" + roadSigns.getId());
			roadSignsRepository.save(roadSigns);

		} else {
			returnRoadSignsDto.setSelf("Exists");

		}
		return returnRoadSignsDto;
	}

	public RecordDto saveNewSign1(RecordDto roadSignsDto) {

		RecordDto returnRoadSignsDto = new RecordDto();
		RoadSigns roadSigns = new RoadSigns();

		if (roadSignsRepository.findByLatLan(roadSignsDto.getLatitude(), roadSignsDto.getLongitude()).size() == 0) {

			roadSigns.setLatitude(roadSignsDto.getLatitude());
			roadSigns.setLongitude(roadSignsDto.getLongitude());
			roadSigns.setSign(roadSignsDto.getSign());
			roadSigns.setMessage(roadSignsDto.getMessage());
			roadSigns.setIsConfirmed(roadSignsDto.getIsConfirmed());

			returnRoadSignsDto.setLatitude(roadSigns.getLatitude());
			returnRoadSignsDto.setLongitude(roadSigns.getLongitude());
			returnRoadSignsDto.setSign(roadSigns.getSign());
			returnRoadSignsDto.setMessage(roadSigns.getMessage());
			returnRoadSignsDto.setIsConfirmed(roadSigns.getIsConfirmed());
			returnRoadSignsDto.setReporterId(roadSigns.getReporterId());

			returnRoadSignsDto.setSelf("http://localhost:8081/team8/sign" + roadSigns.getId());
			roadSignsRepository.save(roadSigns);

		} else {
			returnRoadSignsDto.setSelf("Exists");

		}
		return returnRoadSignsDto;
	}

	public List<RoadSigns> getRoadSign(RecordDto roadSignsDto) {

		List<RoadSigns> roadSigns = new ArrayList<RoadSigns>();

		if (roadSignsRepository
				.findByRadius(roadSignsDto.getLatitude(), roadSignsDto.getLongitude(), roadSignsDto.getRadius())
				.size() != 0) {
			roadSigns = roadSignsRepository.findByRadius(roadSignsDto.getLatitude(), roadSignsDto.getLongitude(),
					roadSignsDto.getRadius());

			return roadSigns;
		} else {
			return roadSigns;
		}
	}

	public List<RoadSigns> getRoadSigns() {

		List<RoadSigns> roadSigns = new ArrayList<RoadSigns>();

		if (roadSignsRepository.findAllSigns().size() != 0) {
			roadSigns = roadSignsRepository.findAllSigns();

			return roadSigns;
		} else {
			return roadSigns;
		}
	}

	public RecordDto addNewSpeedLimitPoint(RecordDto speedLimitDto) {

		RecordDto rtnSpeedLimitDto = new RecordDto();
		SpeedLimit speedLimit = new SpeedLimit();

		if (speedLimitRepository.findSpeedLimitPoint(speedLimitDto.getLatitude(), speedLimit.getLongitude())
				.size() == 0) {

			speedLimit.setLatitude(speedLimitDto.getLatitude());
			speedLimit.setLongitude(speedLimitDto.getLongitude());
			speedLimit.setSpeedLimit(speedLimitDto.getLimit());
			speedLimit.setRadius(speedLimitDto.getRadius());
			speedLimit.setThresholdLimit(speedLimitDto.getThresholdLimit());
			speedLimit.setMessage(speedLimitDto.getMessage());
//			speedLimit.setReporterId(userDTO.getKid());

			rtnSpeedLimitDto.setLatitude(speedLimit.getLatitude());
			rtnSpeedLimitDto.setLongitude(speedLimit.getLongitude());
			rtnSpeedLimitDto.setLimit(speedLimit.getSpeedLimit());
			rtnSpeedLimitDto.setRadius(speedLimit.getRadius());
			rtnSpeedLimitDto.setThresholdLimit(speedLimit.getThresholdLimit());
			rtnSpeedLimitDto.setMessage(speedLimit.getMessage());
//			rtnSpeedLimitDto.setReporterId(speedLimit.getReporterId());

			speedLimitRepository.save(speedLimit);
			rtnSpeedLimitDto.setSelf("http://localhost:8081/team8/addSpeedLimit" + speedLimit.getId());
			return rtnSpeedLimitDto;
		} else {
			rtnSpeedLimitDto.setSelf("Exists");
			return rtnSpeedLimitDto;
		}
	}

	public List<SpeedLimit> getSpeedLimitPoint(RecordDto speedLimitDto) {

		List<SpeedLimit> speedLimit = new ArrayList<SpeedLimit>();

		if (speedLimitRepository
				.findByRadius(speedLimitDto.getLatitude(), speedLimitDto.getLongitude(), speedLimitDto.getRadius())
				.size() != 0) {
			speedLimit = speedLimitRepository.findByRadius(speedLimitDto.getLatitude(), speedLimitDto.getLongitude(),
					speedLimitDto.getRadius());

			return speedLimit;
		} else {
			return speedLimit;
		}
	}

	public List<SpeedLimit> getSpeedLimitPoints() {

		List<SpeedLimit> speedLimit = new ArrayList<SpeedLimit>();

		if (speedLimitRepository.findAllSpeedLimitedPoints().size() != 0) {
			speedLimit = speedLimitRepository.findAllSpeedLimitedPoints();

			return speedLimit;
		} else {
			return speedLimit;
		}
	}

	public List<Accident> getNotConfirmedAccidents() {
		List<Accident> list = accidentRepository.findAllNotConfirmed();
		return list;
	}

}
