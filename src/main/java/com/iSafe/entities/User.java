package com.iSafe.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//This is the table creation of users
//All the user details will be in first normalization form

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String username;
	private String phonenumber;
	private String email;
	private String nic;
	private String address;
	private Date dob;
	private int isConfirmed;
	
	private Date dateOfIssueLicense;
	private Date dateOfExpireLicense;
	
	private String imageOfDriverUrl;
	private String keycloakId;
	private String licenseNum;
	private String idUrl;

	public String getLicenseNum() {
		return licenseNum;
	}

	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getKeycloakId() {
		return keycloakId;
	}

	public void setKeycloakId(String keycloakId) {
		this.keycloakId = keycloakId;
	}

	public int getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(int isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public String getIdUrl() {
		return idUrl;
	}

	public void setIdUrl(String idUrl) {
		this.idUrl = idUrl;
	}
	
	
}
