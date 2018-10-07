package com.iSafe.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iSafe.entities.User;

public interface UserRepo extends CrudRepository<User, Long> {

	public User findByEmail(String email);

	public User findByKeycloakId(String kid);

	@Query("select u from User u where u.isConfirmed=0")
	public List<User> notConfirmed();
	
	@Query("select u from User u where u.isConfirmed=1")
	public List<User> allUsers();

	@Query("select u from User u where u.username=?1")
	public User findByUserName(String userName);
	
	@Query("select u from User u where u.keycloakId=?1")
	public User findByKid(String id);

	@Modifying
	@Transactional
	@Query("update User set isConfirmed=1 where keycloakId=?1")
	public void confirmUser(String id);

	@Modifying
	@Transactional
	@Query("update User set imageOfDriverUrl=?1,idUrl=?2 where keycloakId=?3")
	public void addUrls(String uUrl, String iUrl, String id);
	
	@Modifying
	@Transactional
	@Query("delete from User where keycloakId=?1")
	public void deleteRecord(String id);

}
