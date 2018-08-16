package com.iSafe.controllers;

import javax.servlet.http.HttpServletRequest;
//import org.keycloak.KeycloakPrincipal;
//import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;

import com.iSafe.models.RecordDto;
import com.iSafe.models.UserDTO;
import com.iSafe.services.KeycloakService;
import com.iSafe.services.RecordService;

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
		
		try {
			int statusId = recordService.confirmOrDeleteRoadSign(recordDto);
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

	// ----------------------Speed Limit(start)-------------------------------//

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
}
