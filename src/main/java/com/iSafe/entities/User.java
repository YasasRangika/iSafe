package com.iSafe.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@NotNull
	private String username;
	@NotNull
	private int phonenumber;
	@NotNull
	private String email;
	@NotNull
	private String nic;
	@NotNull
	private String address;
	@NotNull
	private Date dob;
	@NotNull
	private int isConfirmed;
	
	@NotNull
	private String licenseUrl;
	@NotNull
	private Date dateOfIssueLicense;
	@NotNull
	private Date dateOfExpireLicense;
	@NotNull
	private String imageOfDriverUrl;
	
	private String keycloakId;

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

	public int getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(int phonenumber) {
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
	
	
}
