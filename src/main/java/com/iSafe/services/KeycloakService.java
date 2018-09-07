package com.iSafe.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.iSafe.entities.User;
import com.iSafe.models.UserCredential;
import com.iSafe.models.UserDTO;
import com.iSafe.repositories.UserRepo;

@Component
public class KeycloakService {

	@Value("${keycloak.credentials.secret}")
	private String SECRETKEY;

	@Value("${keycloak.resource}")
	private String CLIENTID;

	@Value("${keycloak.auth-server-url}")
	private String AUTHURL;

	@Value("${keycloak.realm}")
	private String REALM;

//	@Value("${mykeycloak.user}")
//	private String USER;
//	
//	@Value("${mykeycloak.password}")
//	private String PASSWORD;

	@Autowired
	UserService userService;

	public String getToken(UserCredential userCredential) {
		String responseToken = null;
		try {
//			System.out.println(userCredential.getPassword()+"  "+userCredential.getUsername());
			String username = userCredential.getUsername();

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("grant_type", "password"));
			urlParameters.add(new BasicNameValuePair("client_id", CLIENTID));
			urlParameters.add(new BasicNameValuePair("username", username));
			urlParameters.add(new BasicNameValuePair("password", userCredential.getPassword()));
			urlParameters.add(new BasicNameValuePair("client_secret", SECRETKEY));

			responseToken = sendPost(urlParameters);
//			System.out.println(responseToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// UserRepresentation user
		return responseToken;

	}

	public String getByRefreshToken(String refreshToken) {

		String responseToken = null;
		try {

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
			urlParameters.add(new BasicNameValuePair("client_id", CLIENTID));
			urlParameters.add(new BasicNameValuePair("refresh_token", refreshToken));
			urlParameters.add(new BasicNameValuePair("client_secret", SECRETKEY));

			responseToken = sendPost(urlParameters);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return responseToken;
	}

	private String sendPost(List<NameValuePair> urlParameters) throws Exception {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(AUTHURL + "/realms/" + REALM + "/protocol/openid-connect/token");

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = client.execute(post);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}

	public int createUserInKeyCloak(UserDTO userDTO) {

		int statusId = 0;
		try {

			UsersResource userRessource = getKeycloakUserResource();

			UserRepresentation user = new UserRepresentation();
			user.setUsername(userDTO.getUsername());
			user.setEmail(userDTO.getEmail());
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());

			user.setEnabled(false);

//			//user.setA
//			user.setEnabled(false);
//			
			// Create user
			Response result = userRessource.create(user);

			System.out.println("iSafe Keycloak create user response code>>>>" + result.getStatus());

			statusId = result.getStatus();

			if (statusId == 201) {

				String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

				System.out.println(
						"iSafe User created with u\n" + "\n" + "import og.api.document.UserDoc;serId:" + userId);

				User user2 = new User();
				user2.setKeycloakId(userId);
				user2.setUsername(userDTO.getUsername());
				user2.setEmail(userDTO.getEmail());
				user2.setPhonenumber(userDTO.getPhonenumber());
				user2.setNic(userDTO.getNic());
				user2.setAddress(userDTO.getAddress());
				user2.setDob(userDTO.getDob());
				user2.setLicenseUrl(userDTO.getLicenseUrl());
				user2.setDateOfIssueLicense(userDTO.getDateOfIssueLicense());
				user2.setDateOfExpireLicense(userDTO.getDateOfExpireLicense());
				user2.setImageOfDriverUrl(userDTO.getImageOfDriverUrl());
				user2.setIsConfirmed(0);

				userService.saveUser(user2);

				// Define password credential
				CredentialRepresentation passwordCred = new CredentialRepresentation();
				passwordCred.setTemporary(false);
				passwordCred.setType(CredentialRepresentation.PASSWORD);
				passwordCred.setValue(userDTO.getPassword());

				// Set password credential
				userRessource.get(userId).resetPassword(passwordCred);

				// set role
				RealmResource realmResource = getRealmResource();
				RoleRepresentation savedRoleRepresentation = realmResource.roles().get("user").toRepresentation();
				realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(savedRoleRepresentation));

				System.out.println("Username==" + userDTO.getUsername() + " created in iSafe keycloak successfully");

			}

			else if (statusId == 409) {
				System.out.println("Username==" + userDTO.getUsername() + " already present in iSafe keycloak");

			} else {
				System.out.println("Username==" + userDTO.getUsername() + " could not be created in iSafe keycloak");

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return statusId;

	}

	@Autowired
	UserRepo userRepo;
	
	public boolean confirmUser(String id) {

		UsersResource userRessource = getKeycloakUserResource();
		UserRepresentation user = new UserRepresentation();
//		System.out.println(id);
		
		try {
//			System.out.println("point 1");
//			user = userRessource.get("10a933b5-a3f9-4235-98c1-f45d0d05ef7f").toRepresentation();
//			System.out.println("point 2");
			user.setEnabled(true);
			user.setEmailVerified(true);
			userRessource.get(id).update(user);
//			System.out.println("point 1");
//			System.out.println(id);
			userRepo.confirmUser(id);
//			System.out.println("point 2");
			return true;
		} catch (Exception ex) {
//			System.out.println(ex);
			return false;
		}

	}

	public int createAdminUser(UserDTO userDTO) {

		int statusId = 0;
		try {

			UsersResource userRessource = getKeycloakUserResource();

			UserRepresentation user = new UserRepresentation();
			user.setUsername(userDTO.getUsername());
			user.setEmail(userDTO.getEmail());
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setEnabled(true);

			// user.setA
			user.setEnabled(true);

			// Create user
			Response result = userRessource.create(user);

			System.out.println("iSafe Keycloak create user response code>>>>" + result.getStatus());

			statusId = result.getStatus();

			if (statusId == 201) {

				String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

				System.out.println("iSafe User created with userId:" + userId);

				User user2 = new User();
				user2.setKeycloakId(userId);
				user2.setUsername(userDTO.getUsername());
				user2.setEmail(userDTO.getEmail());
				user2.setPhonenumber(userDTO.getPhonenumber());
				userService.saveUser(user2);

				// Define password credential
				CredentialRepresentation passwordCred = new CredentialRepresentation();
				passwordCred.setTemporary(false);
				passwordCred.setType(CredentialRepresentation.PASSWORD);
				passwordCred.setValue(userDTO.getPassword());

				// Set password credential

				userRessource.get(userId).resetPassword(passwordCred);

				// set role
				RealmResource realmResource = getRealmResource();
				RoleRepresentation savedRoleRepresentation = realmResource.roles().get("admin").toRepresentation();
				realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(savedRoleRepresentation));

				System.out.println(
						"Username==" + userDTO.getUsername() + " created admin in iSafe keycloak successfully");

			}

			else if (statusId == 409) {
				System.out.println("Username==" + userDTO.getUsername() + " admin already present in iSafe keycloak");

			} else {
				System.out.println(
						"Username==" + userDTO.getUsername() + " admin could not be created in iSafe keycloak");

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return statusId;

	}

	protected UsersResource getKeycloakUserResource() {

		Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTHURL).realm("master").username("yasas").password("yasas")
				.clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
				.build();

		RealmResource realmResource = kc.realm(REALM);
		UsersResource userRessource = realmResource.users();

		return userRessource;
	}

	public void logoutUser(String userId) {

		UsersResource userRessource = getKeycloakUserResource();

		userRessource.get(userId).logout();

	}

	public int recreateUserInKeyCloak(UserDTO userDTO) {
		try {
			User userDoc = userService.getUserByEmail(userDTO.getEmail());
			UsersResource userRessource = getKeycloakUserResource();
			userRessource.get(userDoc.getKeycloakId()).remove();
			userService.deleteUser(userDoc);
			return createUserInKeyCloak(userDTO);
		} catch (Exception ex) {
			return 409;
		}

	}

	// Reset password
	public void resetPassword(String newPassword, String userId) {

		UsersResource userResource = getKeycloakUserResource();

		// Define password credential
		CredentialRepresentation passwordCred = new CredentialRepresentation();
		passwordCred.setTemporary(false);
		passwordCred.setType(CredentialRepresentation.PASSWORD);
		passwordCred.setValue(newPassword.toString().trim());

		// Set password credential
		userResource.get(userId).resetPassword(passwordCred);

	}

	private RealmResource getRealmResource() {

		Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTHURL).realm("master").username("yasas").password("yasas")
				.clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
				.build();

		RealmResource realmResource = kc.realm(REALM);

		return realmResource;

	}
}
