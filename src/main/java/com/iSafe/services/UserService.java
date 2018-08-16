package com.iSafe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iSafe.entities.User;
import com.iSafe.repositories.UserRepo;

@Service
public class UserService {

	@Autowired
	UserRepo userRepo;
	
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
}
