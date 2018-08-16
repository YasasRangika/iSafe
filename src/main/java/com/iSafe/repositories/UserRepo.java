package com.iSafe.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.User;


public interface UserRepo extends CrudRepository<User, Long> {

	public User findByEmail(String email);
	
	User findByKeycloakId(String kid);
}
