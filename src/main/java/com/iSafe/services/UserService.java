package com.iSafe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iSafe.entities.User;
import com.iSafe.models.UserDTO;
import com.iSafe.repositories.UserRepo;

@Service
public class UserService {

	@Autowired
	UserRepo userRepo;
	
	public User allDetails(String kid) {
		User u = userRepo.findByKeycloakId(kid);
		return u;
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

	@Autowired
	KeycloakService keycloakService;

	public boolean confirmUser(UserDTO userDTO) {
//		System.out.println(userDTO.getKid());
		boolean status = keycloakService.confirmUser(userDTO.getKid());
		return status;
	}
}
