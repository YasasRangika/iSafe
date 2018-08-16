package com.iSafe.models;

import java.sql.Date;
import java.util.Set;

import lombok.Data;

@Data
public class UserDTO {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private int phonenumber;
	private String nic;
	private String address;
	private Date dob;
	private Set<String> roles;
	
	private String licenseUrl;
	private Date dateOfIssueLicense;
	private Date dateOfExpireLicense;
	private String imageOfDriverUrl;
	
	private String kid;

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

	public int getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(int phonenumber) {
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

	public String getLicenseUrl() {
		return licenseUrl;
	}

	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
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
	
	
}
