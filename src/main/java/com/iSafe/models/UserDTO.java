package com.iSafe.models;

import java.sql.Date;
import java.util.Set;

import lombok.Data;

//This is the JSON format of user data that transfer within the server and the front-end
//This format use to identify what is the structure of received data
//There is no need to fill all the variables include here
//According to the situation set variables and send them to the server

@Data
public class UserDTO {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phonenumber;
	private String nic;
	private String address;
	private Date dob;
	private Set<String> roles;
	private int points;
	
	private Date dateOfIssueLicense;
	private Date dateOfExpireLicense;
	private String licenseNum;
	
	private String imageOfDriverUrl;
	private String idUrl;

	private String kid;
	private int isConfirmed;
	
	public String getLicenseNum() {
		return licenseNum;
	}

	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getNic() {
		return nic;
	}

	public void setNic(String nic) {
		this.nic = nic;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getDateOfIssueLicense() {
		return dateOfIssueLicense;
	}

	public void setDateOfIssueLicense(Date dateOfIssueLicense) {
		this.dateOfIssueLicense = dateOfIssueLicense;
	}

	public Date getDateOfExpireLicense() {
		return dateOfExpireLicense;
	}

	public void setDateOfExpireLicense(Date dateOfExpireLicense) {
		this.dateOfExpireLicense = dateOfExpireLicense;
	}

	public String getImageOfDriverUrl() {
		return imageOfDriverUrl;
	}

	public void setImageOfDriverUrl(String imageOfDriverUrl) {
		this.imageOfDriverUrl = imageOfDriverUrl;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public int getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(int isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getIdUrl() {
		return idUrl;
	}

	public void setIdUrl(String idUrl) {
		this.idUrl = idUrl;
	}
	
}
