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

//All of these api calls are in this AdminController class because of authentication in web is wasn't success with KeyCloak
//So without authentication these api calls can call via your web site
//If you add security constraints to the property class then you can fulfill these functions are already written in UserController class

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private KeycloakService keycloakService;

	// ADMIN USER SIGN UP

	@PostMapping("/create")
	public ResponseEntity<?> addAdminUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
		try {
			int statusId = keycloakService.createAdminUser(userDTO);
			if (statusId == 201)
				return new ResponseEntity<Object>("OK", HttpStatus.CREATED); // User will be created in keycloak server
																				// as well as in server database also.
																				// The isConfirmed value will be added
																				// as 1 and admin user will be created
																				// with full user permissions
			else
				return new ResponseEntity<Object>("User Creating Failed", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Object>("User Creating Failed", HttpStatus.BAD_REQUEST);
		}
	}

	@Autowired
	RecordService recordService;

	// To add new road sign by admin
	// This will permanently added to the database
	// In iSafe web application map drawings are called to this api

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

	// To add new road accident by admin
	// This will permanently added to the database
	// In iSafe web application map drawings are called to this api

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

	// To add new critical point by admin
	// This will permanently added to the database
	// In iSafe web application map drawings are called to this api

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

	// To retrieve road signs from database to appear in web sites map will get
	// through this api
	// All the road signs are getting around radius of 10m

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

	// To retrieve blackspots from database to appear in web sites map will get
	// through this api
	// All the blackspots are getting around radius of 10m

	@PostMapping("/find/blackspot")
	public ResponseEntity<?> getBlackSpots() {

		List<BlackSpot> blackSpotDto2 = recordService.getBlackSpots();

		if (blackSpotDto2.size() > 0) {
			return new ResponseEntity<Object>(blackSpotDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}

	// To retrieve criticalPoints from database to appear in web sites map will get
	// through this api
	// All the criticalPoints are getting around radius of 10m

	@PostMapping("/find/criticalPoint")
	public ResponseEntity<?> getCriticalPoint() {

		List<CriticalPoint> criticalPointDto2 = recordService.getCriticalPoints();

		if (criticalPointDto2.size() > 0) {
			return new ResponseEntity<Object>(criticalPointDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}

	// To retrieve speed limits from database to appear in web sites map will get
	// through this api
	// All the speed limits are getting around radius of 10m

	@PostMapping("/find/speedLimit")
	public ResponseEntity<?> getSpeedLimit() {

		List<SpeedLimit> speedLimitDto2 = recordService.getSpeedLimitPoints();

		if (speedLimitDto2.size() > 0) {
			return new ResponseEntity<Object>(speedLimitDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}

	// To get all kind of record data from database will get through this api call
	// If anyone needs to show all kind of data use this api.
	// Response will be came as a list of lists

	@PostMapping("/find/all")
	public ResponseEntity<?> getAllRecords() {
		return new ResponseEntity<Object>(recordService.allRecords(), HttpStatus.OK);
	}

	// To remove anykind of record
	// Request will be came with Lat, Lon, and one other unique message or
	// mentioning what roadsign delete when road signs

	@PostMapping("/removeRecord")
	public ResponseEntity<?> removeRecord(@RequestBody RecordDto recordDto) {
		int status = recordService.removeRecord(recordDto);
		switch (status) {

		// Each one will be identified and notify to the front

		case 201:
			return new ResponseEntity<Object>("Road Sign Deleted", HttpStatus.OK);
		case 202:
			return new ResponseEntity<Object>("Blackspot Deleted", HttpStatus.OK);
		case 203:
			return new ResponseEntity<Object>("Critical Point Deleted", HttpStatus.OK);
		case 204:
			return new ResponseEntity<Object>("Speed Limit Deleted", HttpStatus.OK);
		default:
			return new ResponseEntity<Object>("Not Found", HttpStatus.BAD_REQUEST);
		}
	}

	// To confirm a road sign added by user use this api

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

	// To confirm a accident added by user use this api

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

	// To add new speed limit point by admin
	// This will permanently added to the database
	// In iSafe web application map drawings are called to this api

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

	// To get list of new not confirmed user requests can get through this api

	@PostMapping("/notConfirmedUsers")
	public ResponseEntity<?> getNotConfirmedUsers() {
		List<User> user = userService.getNotConfirmedUsers();
		if (user.isEmpty())
			return new ResponseEntity<Object>("Task Failed", HttpStatus.BAD_REQUEST);
		else
			return new ResponseEntity<Object>(user, HttpStatus.OK);
	}

	// TO confirm a user call this api with isConfirmed value = 1

	@PostMapping("/confirmUser")
	public ResponseEntity<?> confirmAUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
		if (userService.confirmUser(userDTO)) {
			return new ResponseEntity<Object>("successfully updated status!!", HttpStatus.OK);
		} else
			return new ResponseEntity<Object>("Error occured while processing!!!", HttpStatus.BAD_REQUEST);
	}

	// To list all the users in database can get through this api

	@PostMapping("/allUsers")
	public ResponseEntity<?> getAllUsers() {
		List<User> user = userService.getAllUsers();
		if (user.isEmpty())
			return new ResponseEntity<Object>("Task Failed", HttpStatus.BAD_REQUEST);
		else
			return new ResponseEntity<Object>(user, HttpStatus.OK);
	}

	// Token data can decrypt using this api
	// If authentication success and token can be successfully transfer to the
	// backend in header Http request, then use this method to get token data

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

	//This returns all the not confirmed user added accident records
	
	@PostMapping("/notConfirmedAccidents")
	public ResponseEntity<?> getNotConfirmedAccidents() {
		List<Accident> accident = recordService.getNotConfirmedAccidents();
		if (accident.isEmpty())
			return new ResponseEntity<Object>("Task Failed", HttpStatus.BAD_REQUEST);
		else
			return new ResponseEntity<Object>(accident, HttpStatus.OK);
	}
}
