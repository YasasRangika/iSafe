package com.iSafe.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.User;


public interface UserRepo extends CrudRepository<User, Long> {

	public User findByEmail(String email);
	
	User findByKeycloakId(String kid);
	
	@Query("select u from User u where u.isConfirmed=0")
	public List<User> notConfirmed();
	
	@Modifying
	@Transactional
	@Query("update User set isConfirmed=1 where keycloakId=?1")
	public void confirmUser(String id);
}
