package com.iSafe.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iSafe.entities.Accident;
import com.iSafe.entities.BlackSpot;
import com.iSafe.entities.CriticalPoint;
import com.iSafe.entities.RoadSigns;
import com.iSafe.entities.SpeedLimit;
import com.iSafe.models.RecordDto;
import com.iSafe.models.RecordsOnPathDto;
import com.iSafe.models.UserCredential;
import com.iSafe.models.UserDTO;
import com.iSafe.services.KeycloakService;
import com.iSafe.services.RecordService;
import com.iSafe.services.UserService;

//All the open user activities can do using this api s

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/open")
public class OpenController {

	//To check server is working
	
	@RequestMapping("")
	public ResponseEntity<?> index() {
		return new ResponseEntity<Object>("Welcome!", HttpStatus.OK);
	}

	@Autowired
	private KeycloakService keycloakService;
	@Autowired
	private UserService userServices;

	//This api use by android application to add user photo and ID card photo
	
	@RequestMapping(value = "/addUrls", method = RequestMethod.POST)
	public ResponseEntity<?> setUrls(@RequestBody UserDTO userDTO) {
		boolean status = userServices.updateUrls(userDTO);
//		System.out.println(userDTO.getIdUrl());
		if (status) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	//This api use to get token of the user that passes via http request as well as verify the login too.
	//Need username and password as credentials to take the token.
	
	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public ResponseEntity<?> getTokenUsingCredentials(@RequestBody UserDTO userDTO) {
		System.out.println("Get Token");

		String responseToken = null;
		try {

			responseToken = keycloakService.getToken(new UserCredential(userDTO.getUsername(), userDTO.getPassword()));

		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(responseToken, HttpStatus.OK);

	}
	
	//This api returns the user id according to the user details provided by fron end
	
	@RequestMapping(value = "/userID", method = RequestMethod.POST)
	public ResponseEntity<?> getUserID(@RequestBody UserDTO userDTO) {
		String userId = null;
		try {

			userId = keycloakService.getUserId(new UserCredential(userDTO.getUsername(), userDTO.getPassword()));

		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(userId, HttpStatus.OK);

	}
	
	//If token is expired while using iSafe this api will generate new token

	@RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
	public ResponseEntity<?> getTokenUsingRefreshToken(@RequestHeader(value = "Authorization") String refreshToken) {

		String responseToken = null;
		try {

			responseToken = keycloakService.getByRefreshToken(refreshToken);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(responseToken, HttpStatus.OK);

	}

	//This api is same as in same api calls in admin path
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {

		String userId = null;
		try {

			int statusId = keycloakService.createUserInKeyCloak(userDTO);
			userId = keycloakService.getUserId(new UserCredential(userDTO.getUsername(), userDTO.getPassword()));

			if (statusId == 201)
				return new ResponseEntity<>(userId, HttpStatus.OK);
			else
				return new ResponseEntity<Object>("User Creating Failed", HttpStatus.BAD_REQUEST);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		}

	}

	@RequestMapping(value = "/recreate", method = RequestMethod.POST)
	public ResponseEntity<?> recreateUser(@RequestBody UserDTO userDTO) {
		int statusId = keycloakService.recreateUserInKeyCloak(userDTO);
		if (statusId == 201)
			return new ResponseEntity<Object>("OK", HttpStatus.CREATED);
		else
			return new ResponseEntity<Object>("User Creating Failed", HttpStatus.BAD_REQUEST);
	}

	//To logout from the session
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ResponseEntity<?> logoutUser(HttpServletRequest request) {

		request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		AccessToken token = ((KeycloakPrincipal<?>) request.getUserPrincipal()).getKeycloakSecurityContext().getToken();

		String userId = token.getSubject();

		keycloakService.logoutUser(userId);

		return new ResponseEntity<>("Hi!, you have logged out successfully!", HttpStatus.OK);

	}
	
	//To update the password of the user
	//User passwords are kept in keycloak server and no one can view them

	@RequestMapping(value = "/update/password", method = RequestMethod.GET)
	public ResponseEntity<?> updatePassword(HttpServletRequest request, String newPassword) {

		request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		AccessToken token = ((KeycloakPrincipal<?>) request.getUserPrincipal()).getKeycloakSecurityContext().getToken();

		String userId = token.getSubject();

		keycloakService.resetPassword(newPassword, userId);

		return new ResponseEntity<>("Hi!, your password has been successfully updated!", HttpStatus.OK);

	}
	
	//These api s are same as in same api calls in admin path

	// ----------------------Road Signs(start)-------------------------------//

	@Autowired
	RecordService roadSignService;

	@PostMapping("/find/roadsign")
	public ResponseEntity<?> getRaodSigns(@RequestBody RecordDto roadSignsDto, HttpServletRequest request) {
		List<RoadSigns> roadSignsDto2 = roadSignService.getRoadSign(roadSignsDto);
//		System.out.println("Point 1");
		if (roadSignsDto2.size() > 0) {
//			System.out.println("Point 2");
			return new ResponseEntity<Object>(roadSignsDto2, HttpStatus.OK);
			// return ResponseEntity.ok(roadSignsDto2);
		} else {
//			System.out.println("Point 3");
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}
	// ----------------------Road Signs(end)-------------------------------//

	// ----------------------Incident(start)-------------------------------//

	@Autowired
	RecordService incidentService;

	@PostMapping("/find/accident")
	public ResponseEntity<?> findIncident(@RequestBody RecordDto incidentDto, HttpServletRequest request) {
		List<Accident> incidentDto2 = incidentService.getIncident(incidentDto);
		if (incidentDto2.size() > 0) {
			return new ResponseEntity<Object>(incidentDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}
	// ----------------------Incident(end)-------------------------------//

	// ----------------------Records On Path(start)-------------------------------//

//	@Autowired
//	RecordService recordOnPathService;
//
//	@PostMapping("/find/records")
//	public ResponseEntity<?> findRecordsOnPath(@RequestBody RecordDto latLngDto, HttpServletRequest request) {
//		RecordDto recordsOnPathDto = recordOnPathService.updateMap(latLngDto);
//		if (recordsOnPathDto.getSelf() == "Exists") {
//			return new ResponseEntity<Object>("Found!", HttpStatus.OK);
//		} else {
//			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
//		}
//	}

	// ----------------------Records On Path(end)-------------------------------//

	// ----------------------Black Spots(start)-------------------------------//

	@Autowired
	RecordService blackSpotService;

	@PostMapping("/find/blackspot")
	public ResponseEntity<?> getBlackSpots(@RequestBody RecordDto blackSpotDto, HttpServletRequest request) {

		List<BlackSpot> blackSpotDto2 = blackSpotService.getBlackSpot(blackSpotDto);

		if (blackSpotDto2.size() > 0) {
			return new ResponseEntity<Object>(blackSpotDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}
	// ----------------------Black Spots(end)-------------------------------//

	// ----------------------Critical Point(start)-------------------------------//

	@Autowired
	RecordService criticalPointService;

	@PostMapping("/find/criticalPoint")
	public ResponseEntity<?> getCriticalPoint(@RequestBody RecordDto criticalPointDto, HttpServletRequest request) {

		List<CriticalPoint> criticalPointDto2 = criticalPointService.getCriticalPoint(criticalPointDto);

		if (criticalPointDto2.size() > 0) {
			return new ResponseEntity<Object>(criticalPointDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}
	// ----------------------Critical Point(end)-------------------------------//

	// ----------------------Speed Limit(start)-------------------------------//

	@Autowired
	RecordService speedLimitService;

	@PostMapping("/find/speedLimit")
	public ResponseEntity<?> getSpeedLimit(@RequestBody RecordDto speedLimitDto, HttpServletRequest request) {

		List<SpeedLimit> speedLimitDto2 = speedLimitService.getSpeedLimitPoint(speedLimitDto);

		if (speedLimitDto2.size() > 0) {
			return new ResponseEntity<Object>(speedLimitDto2, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("No matches!", HttpStatus.BAD_REQUEST);
		}
	}
	// ----------------------Speed Limit(end)-------------------------------//

	@Autowired
	RecordService recordService;

	@PostMapping("/find/recordsonpath")
	public ResponseEntity<?> getrecordsOnPath(@RequestBody List<RecordDto> recordDto, HttpServletRequest request) {

		RecordsOnPathDto list = recordService.pointsOnRoad(recordDto);

		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
}
