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
import com.iSafe.entities.User;
import com.iSafe.models.RecordDto;
import com.iSafe.models.UserDTO;
import com.iSafe.services.KeycloakService;
import com.iSafe.services.RecordService;
import com.iSafe.services.UserService;

@CrossOrigin(origins = "*")
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
			return new ResponseEntity<Object>("successfully confirmed!!", HttpStatus.OK);
		} else
			return new ResponseEntity<Object>("Error occured while confirming!!!", HttpStatus.BAD_REQUEST);
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
