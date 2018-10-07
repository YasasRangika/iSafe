package com.iSafe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iSafe.entities.Point;
import com.iSafe.entities.User;
import com.iSafe.models.UserDTO;
import com.iSafe.repositories.PointsRepository;
import com.iSafe.repositories.UserRepo;

@Service
public class UserService {

	@Autowired
	UserRepo userRepo;
	@Autowired
	PointsRepository pointsRepository;

	public UserDTO allDetails(String kid) {
		User u = userRepo.findByKeycloakId(kid);
		Point p = pointsRepository.getPointsOfUser(kid);
		UserDTO userDTO = new UserDTO();

		userDTO.setUsername(u.getUsername());
		userDTO.setPhonenumber(u.getPhonenumber());
		userDTO.setEmail(u.getEmail());
		userDTO.setNic(u.getNic());
		userDTO.setAddress(u.getAddress());
		userDTO.setDob(u.getDob());
		userDTO.setDateOfIssueLicense(u.getDateOfIssueLicense());
		userDTO.setDateOfExpireLicense(u.getDateOfExpireLicense());
		userDTO.setImageOfDriverUrl(u.getImageOfDriverUrl());
		userDTO.setKid(u.getKeycloakId());
		userDTO.setLicenseNum(u.getLicenseNum());
		userDTO.setIdUrl(u.getIdUrl());
		userDTO.setPoints(p.getPoints());
		return userDTO;
	}

	public boolean updateUrls(UserDTO userDTO) {
		try {
			userRepo.addUrls(userDTO.getImageOfDriverUrl(), userDTO.getIdUrl(), userDTO.getKid());
//			System.out.println(userDTO.getLicenseUrl());
//			System.out.println(userDTO.getIdUrl());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public User saveUser(User user) {
		System.out.println("Saving...");
		return userRepo.save(user);
	}

	public List<User> findAll() {
		return (List<User>) userRepo.findAll();
	}

	public User getUserByEmail(String email) {
		User u = userRepo.findByEmail(email);
		return u;
	}

	public void deleteUser(User user) {
		userRepo.delete(user);
	}

	public List<User> getNotConfirmedUsers() {
		List<User> usr = userRepo.notConfirmed();
//		System.out.println("Done");
//		for(User u:usr) {
//			System.out.println(u);
//		}
		return usr;
	}

	public List<User> getAllUsers() {
		List<User> usr = userRepo.allUsers();
		return usr;
	}

	@Autowired
	KeycloakService keycloakService;

	public boolean confirmUser(UserDTO userDTO) {
//		System.out.println(userDTO.getKid());
		boolean status;
		if (userDTO.getIsConfirmed() == 1) {
			status = keycloakService.confirmUser(userDTO.getKid());
		} else if(userDTO.getIsConfirmed() == 0){
			userRepo.deleteRecord(userDTO.getKid());
			status = true;
		}else{
			status = false;
		}
		return status;
	}
}
