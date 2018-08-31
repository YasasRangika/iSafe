package com.iSafe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.User;


public interface UserRepo extends CrudRepository<User, Long> {

	public User findByEmail(String email);
	
	User findByKeycloakId(String kid);
	
	@Query("select u from User u where u.isConfirmed=0")
	public List<User> notConfirmed();
}
