package com.iSafe.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iSafe.models.RecordDto;
import com.iSafe.models.SafestPathDto;
import com.iSafe.models.UserDTO;
import com.iSafe.services.KeycloakService;
import com.iSafe.services.RecordService;
import com.iSafe.services.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	KeycloakService keycloakService;
	@Autowired
	UserService userService;

	@RequestMapping("hello")
	public ResponseEntity<?> seyHello() {
		return new ResponseEntity<Object>("Hello You are Athorized user.", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/accDetails", method = RequestMethod.POST)
	public ResponseEntity<?> viewDetails(HttpServletRequest request) {
		UserDTO userDTO = this.getTokenData(request);
		UserDTO u = userService.allDetails(userDTO.getKid());
		if(u != null) {
			return new ResponseEntity<Object>(u, HttpStatus.OK);
		}else {
			return new ResponseEntity<Object>("User Inavalid", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/userinfo", method = RequestMethod.POST)
	public ResponseEntity<?> userinfo(HttpServletRequest request) {
		UserDTO userDTO = this.getTokenData(request);
		return new ResponseEntity<Object>(userDTO, HttpStatus.OK);
	}

	// ----------------------Road Signs(start)-------------------------------//

	@Autowired
	RecordService tmproadSignService;

	@PostMapping("/add/sign")
	public ResponseEntity<?> addNewSign(@RequestBody RecordDto roadSignsDto, HttpServletRequest request) {
		UserDTO userDTO = this.getTokenData(request);
		for (Iterator<String> it = userDTO.getRoles().iterator(); it.hasNext();) {
			String f = it.next();
			if (f.equals(new String("admin"))) {
				System.out.println("A admin added record");
				roadSignsDto.setIsConfirmed(1);
				break;
			} else if (f.equals(new String("user")))
				System.out.println("A user added record");
			roadSignsDto.setIsConfirmed(0);
		}
		RecordDto rsd = tmproadSignService.saveNewSign(roadSignsDto, userDTO);

		if (rsd.getSelf() == "Exists") {
			return new ResponseEntity<Object>("Sign already Exists!", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<Object>("Sign added!", HttpStatus.CREATED);
		}
	}

	// ----------------------Road Signs(end)-------------------------------//

	// ----------------------Accident(start)-------------------------------//

	@Autowired
	RecordService tmpIncidentService;

	@Autowired
	RecordService recordService;

	@PostMapping("/check/safestPath")
	public ResponseEntity<?> safestPathOnly(@RequestBody List<List<RecordDto>> records, HttpServletRequest request) {
		
		List<SafestPathDto> safePath = new ArrayList<>();
		
		if(safePath.size() != 0) {
			return new ResponseEntity<Object>(safePath, HttpStatus.OK);
		}else {
			return new ResponseEntity<Object>("No Risks!", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/add/accident")
	public ResponseEntity<?> incidentRecords(@RequestBody RecordDto incidentDto, HttpServletRequest request) {
//		System.out.println("Point A");
		UserDTO userDTO = this.getTokenData(request);
//		System.out.println("Point B");
		for (Iterator<String> it = userDTO.getRoles().iterator(); it.hasNext();) {
			String f = it.next();
			if (f.equals(new String("admin"))) {
				System.out.println("A admin added record");
				incidentDto.setIsConfirmed(1);
				break;
			} else if (f.equals(new String("user")))
				System.out.println("A user added record");
			incidentDto.setIsConfirmed(0);
		}
		RecordDto incidentDto2 = tmpIncidentService.addIncident(incidentDto, userDTO);

		if (incidentDto2.getSelf() == "Exists") {
			return new ResponseEntity<Object>("Accident already Exists!", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<Object>("Accident added!", HttpStatus.CREATED);
		}
	}

	// ----------------------Accident(end)-------------------------------//

	// ----------------------Critical Point(start)-------------------------------//

	@Autowired
	RecordService criticalPointService;

	@PostMapping("/add/criticalpoint")
	public ResponseEntity<?> addNewCP(@RequestBody RecordDto criticalPointDto, HttpServletRequest request) {
		UserDTO userDTO = this.getTokenData(request);
		for (Iterator<String> it = userDTO.getRoles().iterator(); it.hasNext();) {
			String f = it.next();
			if (f.equals(new String("admin"))) {
				System.out.println("A admin added record");
				criticalPointDto.setIsConfirmed(1);
				break;
			} else if (f.equals(new String("user")))
				System.out.println("A user added record");
			criticalPointDto.setIsConfirmed(0);
		}
		RecordDto cpDto = criticalPointService.addNewCriticalPoint(criticalPointDto, userDTO);
		if (cpDto.getSelf() == "Exists") {
			return new ResponseEntity<Object>("Critical point already Exists!", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<Object>("Critical point added!", HttpStatus.CREATED);
		}
	}

	// ----------------------Critical Point(end)-------------------------------//

	public UserDTO getTokenData(HttpServletRequest request) {
		request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		AccessToken token = ((KeycloakPrincipal<?>) request.getUserPrincipal()).getKeycloakSecurityContext().getToken();
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
}
