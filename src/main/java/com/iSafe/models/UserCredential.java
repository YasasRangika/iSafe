package com.iSafe.models;

import lombok.Data;

//To pass user credentials as JSON format

@Data
public class UserCredential {

	private String username;
	private String password;
	
	public UserCredential(String username2, String password2) {
		username = username2;
		password = password2;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
