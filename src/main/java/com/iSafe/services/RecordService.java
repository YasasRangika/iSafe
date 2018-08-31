package com.iSafe.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iSafe.entities.Accident;
import com.iSafe.entities.BlackSpot;
import com.iSafe.entities.CriticalPoint;
import com.iSafe.entities.RoadSigns;
import com.iSafe.entities.SpeedLimit;
import com.iSafe.models.RecordDto;
import com.iSafe.models.RecordsOnPathDto;
import com.iSafe.models.UserDTO;
import com.iSafe.repositories.AccidentRepository;
import com.iSafe.repositories.BlackSpotRepository;
import com.iSafe.repositories.CriticalPointRepository;
import com.iSafe.repositories.RoadSignsRepository;
import com.iSafe.repositories.SpeedLimitRepository;

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

	public int confirmOrDeleteAccident(RecordDto recordDto) {
		if (accidentRepository.findByLatLanNotConfirmed(recordDto.getLatitude(), recordDto.getLongitude()) != null) {
			if (recordDto.getIsConfirmed() == 1) {
				accidentRepository.confirmRecord();
				return 1;
			} else {
				accidentRepository.deleteRecord(recordDto.getLatitude(), recordDto.getLongitude());
				return 2;
			}
		} else {
			return 0;
		}
	}

	public int confirmOrDeleteRoadSign(RecordDto recordDto) {
		System.out.println("Line 0");
		if (roadSignsRepository.findByLatLanNotConfirmed(recordDto.getLatitude(), recordDto.getLongitude()) != null) {
			System.out.println("Line 1");
			if (recordDto.getIsConfirmed() == 1) {
				System.out.println("Line 2");
				roadSignsRepository.confirmRecord(recordDto.getLatitude(), recordDto.getLongitude());
				return 200;
			} else {
				System.out.println("Line 3");
				roadSignsRepository.deleteRecord(recordDto.getLatitude(), recordDto.getLongitude());
				return 201;
			}
		} else {
			System.out.println("Line 4");
			return 208;
		}
	}

	public RecordDto addNewCriticalPoint(RecordDto criticalPointDto, UserDTO userDTO) {

		RecordDto rtnCriticalPointDto = new RecordDto();
		CriticalPoint criticalPoint = new CriticalPoint();

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

		if (criticalPointRepository.findCriticalPoint(criticalPoint.getLatitude(),
				criticalPoint.getLongitude()) == null) {
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
				criticalPointDto.getRadius()) != null) {

			criticalPoint = criticalPointRepository.findByRadius(criticalPointDto.getLatitude(),
					criticalPointDto.getLongitude(), criticalPointDto.getRadius());

			return criticalPoint;
		} else {
			return criticalPoint;
		}
	}

	public RecordDto addIncident(RecordDto incidentDto, UserDTO userDTO) {

		RecordDto returnIncidentDto = new RecordDto();
		Accident incident = new Accident();

		incident.setLat(incidentDto.getLatitude());
		incident.setLng(incidentDto.getLongitude());
		incident.setAccident(incidentDto.getAccidentDesc());
		incident.setAccidentType(incidentDto.getAccidentType());
		incident.setDate(incidentDto.getDate());
		incident.setReporter(incidentDto.getReporter());
		incident.setIsConfirmed(incidentDto.getIsConfirmed());
		incident.setReporterId(userDTO.getKid());

		returnIncidentDto.setLatitude(incident.getLat());
		returnIncidentDto.setLongitude(incident.getLng());
		returnIncidentDto.setAccidentDesc(incident.getAccident());
		returnIncidentDto.setAccidentType(incident.getAccidentType());
		returnIncidentDto.setDate(incident.getDate());
		returnIncidentDto.setReporter(incident.getReporter());
		returnIncidentDto.setIsConfirmed(incident.getIsConfirmed());
		returnIncidentDto.setReporterId(incident.getReporterId());

		if (accidentRepository.findByLatLan(incident.getLat(), incident.getLng()) == null) {
			accidentRepository.save(incident);
			returnIncidentDto.setSelf("http://localhost:8081/team8/incident" + incident.getId());
			return returnIncidentDto;
		} else {
			returnIncidentDto.setSelf("Exists");
			return returnIncidentDto;
		}
	}

	public List<Accident> getIncident(RecordDto incidentDto) {

		List<Accident> incident = new ArrayList<Accident>();

		if (accidentRepository.findByRadius(incidentDto.getLatitude(), incidentDto.getLongitude(),
				incidentDto.getRadius()) != null) {
			incident = accidentRepository.findByRadius(incidentDto.getLatitude(), incidentDto.getLongitude(),
					incidentDto.getRadius());

			return incident;
		} else {
			return incident;
		}
	}

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
			if (blackSpotRepository.findBlackSpot(recordDto.getLatitude(), recordDto.getLongitude()) != null)
				blc.add(blackSpotRepository.findBlackSpot(recordDto.getLatitude(), recordDto.getLongitude()));
			if (criticalPointRepository.findCriticalPoint(recordDto.getLatitude(), recordDto.getLongitude()) != null)
				crtcl.add(criticalPointRepository.findCriticalPoint(recordDto.getLatitude(), recordDto.getLongitude()));
			if (roadSignsRepository.findByLatLan(recordDto.getLatitude(), recordDto.getLongitude()) != null)
				rdsign.add(roadSignsRepository.findByLatLan(recordDto.getLatitude(), recordDto.getLongitude()));
			if (speedLimitRepository.findSpeedLimitPoint(recordDto.getLatitude(), recordDto.getLongitude()) != null)
				spd.add(speedLimitRepository.findSpeedLimitPoint(recordDto.getLatitude(), recordDto.getLongitude()));
		}
//		if (acc != null)
//			recordsOnPathDto.setAccidents(acc);
		if (blc != null)
			recordsOnPathDto.setBlackSpots(blc);
		if (crtcl != null)
			recordsOnPathDto.setCriticalPoints(crtcl);
		if (rdsign != null)
			recordsOnPathDto.setRoadSigns(rdsign);
		if (spd != null)
			recordsOnPathDto.setSpeedLimits(spd);

		return recordsOnPathDto;
	}

	public RecordDto updateMap(RecordDto latLngDto) {

		RecordDto recordsOnPathDto = new RecordDto();
		Accident incident = new Accident();
		RoadSigns roadSigns = new RoadSigns();
		BlackSpot blackSpot = new BlackSpot();
		CriticalPoint criticalPoint = new CriticalPoint();
		SpeedLimit speedLimit = new SpeedLimit();

		if (roadSignsRepository.findByLatLan(latLngDto.getLatitude(), latLngDto.getLongitude()) != null
				| accidentRepository.findByLatLan(latLngDto.getLatitude(), latLngDto.getLongitude()) != null
				| blackSpotRepository.findBlackSpot(latLngDto.getLatitude(), latLngDto.getLongitude()) != null
				| criticalPointRepository.findCriticalPoint(latLngDto.getLatitude(), latLngDto.getLongitude()) != null
				| speedLimitRepository.findSpeedLimitPoint(latLngDto.getLatitude(), latLngDto.getLongitude()) != null) {

			recordsOnPathDto.setSelf("Exists");
			roadSigns = roadSignsRepository.findByLatLan(latLngDto.getLatitude(), latLngDto.getLongitude());
			recordsOnPathDto.setRoadsigns(roadSigns);
			incident = accidentRepository.findByLatLan(latLngDto.getLatitude(), latLngDto.getLongitude());
			recordsOnPathDto.setIncident(incident);
			blackSpot = blackSpotRepository.findBlackSpot(latLngDto.getLatitude(), latLngDto.getLongitude());
			recordsOnPathDto.setBlackSpot(blackSpot);
			criticalPoint = criticalPointRepository.findCriticalPoint(latLngDto.getLatitude(),
					latLngDto.getLongitude());
			recordsOnPathDto.setCriticalPoint(criticalPoint);
			speedLimit = speedLimitRepository.findSpeedLimitPoint(latLngDto.getLatitude(), latLngDto.getLongitude());
			recordsOnPathDto.setSpeedLimit(speedLimit);

			return recordsOnPathDto;
		} else {

			recordsOnPathDto.setSelf("Error");
			return recordsOnPathDto;
		}
	}

	public List<BlackSpot> getBlackSpot(RecordDto blackSpotDto) {

		List<BlackSpot> blackSpot = new ArrayList<BlackSpot>();

		if (blackSpotRepository.findByRadius(blackSpotDto.getLatitude(), blackSpotDto.getLongitude(),
				blackSpotDto.getRadius()) != null) {

			blackSpot = blackSpotRepository.findByRadius(blackSpotDto.getLatitude(), blackSpotDto.getLongitude(),
					blackSpotDto.getRadius());

			return blackSpot;
		} else {
			return blackSpot;
		}
	}

	public RecordDto saveNewSign(RecordDto roadSignsDto, UserDTO userDTO) {

		RecordDto returnRoadSignsDto = new RecordDto();

		RoadSigns roadSigns = new RoadSigns();
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

		if (roadSignsRepository.findByLatLan(roadSigns.getLatitude(), roadSigns.getLongitude()) == null) {
			returnRoadSignsDto.setSelf("http://localhost:8081/team8/sign" + roadSigns.getId());
			roadSignsRepository.save(roadSigns);

		} else {
			returnRoadSignsDto.setSelf("Exists");

		}
		return returnRoadSignsDto;
	}

	public List<RoadSigns> getRoadSign(RecordDto roadSignsDto) {

		List<RoadSigns> roadSigns = new ArrayList<RoadSigns>();

		if (roadSignsRepository.findByRadius(roadSignsDto.getLatitude(), roadSignsDto.getLongitude(),
				roadSignsDto.getRadius()) != null) {
			roadSigns = roadSignsRepository.findByRadius(roadSignsDto.getLatitude(), roadSignsDto.getLongitude(),
					roadSignsDto.getRadius());

			return roadSigns;
		} else {
			return roadSigns;
		}
	}

	public RecordDto addNewSpeedLimitPoint(RecordDto speedLimitDto) {

		RecordDto rtnSpeedLimitDto = new RecordDto();
		SpeedLimit speedLimit = new SpeedLimit();

		speedLimit.setLatitude(speedLimitDto.getLatitude());
		speedLimit.setLongitude(speedLimit.getLongitude());
		speedLimit.setSpeedLimit(speedLimitDto.getLimit());
		speedLimit.setRadius(speedLimitDto.getRadius());
		speedLimit.setThresholdLimit(speedLimitDto.getThresholdLimit());
		speedLimit.setMessage(speedLimitDto.getMessage());
//		speedLimit.setReporterId(userDTO.getKid());

		rtnSpeedLimitDto.setLatitude(speedLimit.getLatitude());
		rtnSpeedLimitDto.setLongitude(speedLimit.getLongitude());
		rtnSpeedLimitDto.setLimit(speedLimit.getSpeedLimit());
		rtnSpeedLimitDto.setRadius(speedLimit.getRadius());
		rtnSpeedLimitDto.setThresholdLimit(speedLimit.getThresholdLimit());
		rtnSpeedLimitDto.setMessage(speedLimit.getMessage());
//		rtnSpeedLimitDto.setReporterId(speedLimit.getReporterId());

		if (speedLimitRepository.findSpeedLimitPoint(speedLimit.getLatitude(), speedLimit.getLongitude()) == null) {
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

		if (speedLimitRepository.findByRadius(speedLimitDto.getLatitude(), speedLimitDto.getLongitude(),
				speedLimitDto.getRadius()) != null) {
			speedLimit = speedLimitRepository.findByRadius(speedLimitDto.getLatitude(), speedLimitDto.getLongitude(),
					speedLimitDto.getRadius());

			return speedLimit;
		} else {
			return speedLimit;
		}
	}
//	private double getDistance(LatLng point1, LatLng point2) {
//        double p = 0.017453292519943295;
//        double a = 0.5 - Math.cos((point2.latitude - point1.latitude) * p)/2 +
//                Math.cos(point1.latitude * p) * Math.cos(point2.latitude * p) *
//                        (1 - Math.cos((point2.longitude - point1.longitude) * p))/2;
//
//        return 12.742 * Math.asin(Math.sqrt(a))*1000*1000;
//    }

//	private double getSignDistance(RecordDto recordDto, RoadSigns roadSigns) {
//        double p = 0.017453292519943295;
//        double a = 0.5 - Math.cos((point2.latitude - point1.latitude) * p)/2 +
//                Math.cos(point1.latitude * p) * Math.cos(point2.latitude * p) *
//                        (1 - Math.cos((point2.longitude - point1.longitude) * p))/2;
//
//        return 12.742 * Math.asin(Math.sqrt(a))*1000*1000;
//    }

}
