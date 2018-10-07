package com.iSafe.controllers;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
//import org.keycloak.KeycloakPrincipal;
//import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iSafe.entities.Accident;
import com.iSafe.entities.BlackSpot;
import com.iSafe.entities.CriticalPoint;
import com.iSafe.entities.RoadSigns;
import com.iSafe.entities.SpeedLimit;
import com.iSafe.entities.User;
import com.iSafe.models.RecordDto;
import com.iSafe.models.UserDTO;
import com.iSafe.services.KeycloakService;
import com.iSafe.services.RecordService;
import com.iSafe.services.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private KeycloakService keycloakService;

	@PostMapping("/create")
	public ResponseEntity<?> addAdminUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
		try {
			int statusId = keycloakService.createAdminUser(userDTO);
			if (statusId == 201)
				return new ResponseEntity<Object>("OK", HttpStatus.CREATED);
			else
				return new ResponseEntity<Object>("User Creating Failed", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Object>("User Creating Failed", HttpStatus.BAD_REQUEST);
		}
	}

	@Autowired
	RecordService recordService;

	@PostMapping("/add/sign")
	public ResponseEntity<?> addNewSign(@RequestBody RecordDto roadSignsDto, HttpServletRequest request) {

		roadSignsDto.setIsConfirmed(1);
		RecordDto rsd = recordService.saveNewSign1(roadSignsDto);

		if (rsd.getSelf() == "Exists") {
			return new ResponseEntity<Object>("Sign already Exists!", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<Object>("Sign added!", HttpStatus.CREATED);
		}
	}


	@PostMapping("/add/accident")
	public ResponseEntity<?> incidentRecords(@RequestBody RecordDto incidentDto, HttpServletRequest request) {
		incidentDto.setIsConfirmed(1);
		RecordDto incidentDto2 = recordService.addIncident1(incidentDto);

		if (incidentDto2.getSelf() == "Exists") {
			return new ResponseEntity<Object>("Accident already Exists!", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<Object>("Accident added!", HttpStatus.CREATED);
		}
	}
	
	@PostMapping("/add/criticalpoint")
	public ResponseEntity<?> addNewCP(@RequestBody RecordDto criticalPointDto, HttpServletRequest request) {

		criticalPointDto.setIsConfirmed(1);

		RecordDto cpDto = recordService.addNewCriticalPoint2(criticalPointDto);
		if (cpDto.getSelf() == "Exists") {
			return new ResponseEntity<Object>("Critical point already Exists!", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<Object>("Critical point added!", HttpStatus.CREATED);
		}
	}
	
	@PostMapping("/find/roadsign")
	public ResponseEntity<?> getRaodSigns() {
		List<RoadSigns> roadSignsDto2 = recordService.getRoadSigns();
		if (roadSignsDto2.size() > 0) {
			return new ResponseEntity<Object>(roadSignsDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/find/accident")
	public ResponseEntity<?> findIncident() {
		List<Accident> incidentDto2 = recordService.getIncidents();
		if (incidentDto2.size() > 0) {
			return new ResponseEntity<Object>(incidentDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/find/blackspot")
	public ResponseEntity<?> getBlackSpots() {

		List<BlackSpot> blackSpotDto2 = recordService.getBlackSpots();

		if (blackSpotDto2.size() > 0) {
			return new ResponseEntity<Object>(blackSpotDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/find/criticalPoint")
	public ResponseEntity<?> getCriticalPoint() {

		List<CriticalPoint> criticalPointDto2 = recordService.getCriticalPoints();

		if (criticalPointDto2.size() > 0) {
			return new ResponseEntity<Object>(criticalPointDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/find/speedLimit")
	public ResponseEntity<?> getSpeedLimit() {

		List<SpeedLimit> speedLimitDto2 = recordService.getSpeedLimitPoints();

		if (speedLimitDto2.size() > 0) {
			return new ResponseEntity<Object>(speedLimitDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/find/all")
	public ResponseEntity<?> getAllRecords(){
		return new ResponseEntity<Object>(recordService.allRecords(),HttpStatus.OK);
	}
	

	@PostMapping("/confirm/roadsign")
	public ResponseEntity<?> confirmOrDeleteSign(@RequestBody RecordDto recordDto, HttpServletRequest request) {

		UserDTO userDTO = this.getAdminTokenData(request);

		try {
			int statusId = recordService.confirmOrDeleteRoadSign(recordDto, userDTO);
			if (statusId == 200)
				return new ResponseEntity<Object>("Confirmed", HttpStatus.OK);
			else if (statusId == 201)
				return new ResponseEntity<Object>("Deleted", HttpStatus.OK);
			else
				return new ResponseEntity<Object>("Not Found", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Object>("Task Failed", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/confirm/accident")
	public ResponseEntity<?> confirmOrDeleteAccident(@RequestBody RecordDto recordDto, HttpServletRequest request) {

		UserDTO userDTO = this.getAdminTokenData(request);

		try {
			int statusId = recordService.confirmOrDeleteAccident(recordDto, userDTO);
			if (statusId == 200)
				return new ResponseEntity<Object>("Confirmed", HttpStatus.OK);
			else if (statusId == 201)
				return new ResponseEntity<Object>("Deleted", HttpStatus.OK);
			else
				return new ResponseEntity<Object>("Not Found", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Object>("Task Failed", HttpStatus.BAD_REQUEST);
		}
	}

	// -----------------------Speed Limit(start)--------------------------------//

	@Autowired
	RecordService speedLimitService;

	@PostMapping("/add/speedlimit")
	public ResponseEntity<?> addNewSL(@RequestBody RecordDto speedLimitDto, HttpServletRequest request) {

		RecordDto slDto = speedLimitService.addNewSpeedLimitPoint(speedLimitDto);

		if (slDto.getSelf() == "Exists") {
			return new ResponseEntity<Object>("Speed Limit already Exists!", HttpStatus.IM_USED);
		} else {
			return new ResponseEntity<Object>("Speed Limit added!", HttpStatus.CREATED);
		}

	}

	@Autowired
	UserService userService;

	@PostMapping("/notConfirmedUsers")
	public ResponseEntity<?> getNotConfirmedUsers() {
		List<User> user = userService.getNotConfirmedUsers();
		if (user.isEmpty())
			return new ResponseEntity<Object>("Task Failed", HttpStatus.BAD_REQUEST);
		else
			return new ResponseEntity<Object>(user, HttpStatus.OK);
	}

	@PostMapping("/confirmUser")
	public ResponseEntity<?> confirmAUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
		if (userService.confirmUser(userDTO)) {
			return new ResponseEntity<Object>("successfully updated status!!", HttpStatus.OK);
		} else
			return new ResponseEntity<Object>("Error occured while processing!!!", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/allUsers")
	public ResponseEntity<?> getAllUsers() {
		List<User> user = userService.getAllUsers();
		if (user.isEmpty())
			return new ResponseEntity<Object>("Task Failed", HttpStatus.BAD_REQUEST);
		else
			return new ResponseEntity<Object>(user, HttpStatus.OK);
	}

	private UserDTO getAdminTokenData(HttpServletRequest request) {

		request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//		System.out.println("Point 01");
		AccessToken token = ((KeycloakPrincipal<?>) request.getUserPrincipal()).getKeycloakSecurityContext().getToken();
//		System.out.println("Point 02");
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(token.getEmail());
		userDTO.setUsername(token.getPreferredUsername());
		userDTO.setFirstName(token.getGivenName());
		userDTO.setLastName(token.getFamilyName());
		userDTO.setKid(token.getAccessTokenHash());
		// userDTO.setAddress(token.getAddress());
		// userDTO.setPhonenumber(Integer.parseInt(token.getPhoneNumber()));
		Set<String> roles = token.getRealmAccess().getRoles();
		userDTO.setRoles(roles);
		userDTO.setKid(token.getSubject());
		return userDTO;
	}

	@PostMapping("/notConfirmedAccidents")
	public ResponseEntity<?> getNotConfirmedAccidents() {
		List<Accident> accident = recordService.getNotConfirmedAccidents();
		if (accident.isEmpty())
			return new ResponseEntity<Object>("Task Failed", HttpStatus.BAD_REQUEST);
		else
			return new ResponseEntity<Object>(accident, HttpStatus.OK);
	}
}
