/**
 * 
 */
package com.appsdevelopeblog.app.ws.shared.dto;

import java.io.Serializable;
import java.time.*;
import java.util.List;

/**
 * @author SonaSach
 *
 *         Now, we need to create 1 more class, this class will be shared among
 *         different layers like UI layer, DB layer and service layer, this
 *         class will be used like a Data Transfer Object, it will contain lot
 *         more information and our java code will be passing user information
 *         in this dto, when it needs to send information between different
 *         layers.
 */
public class UserDto implements Serializable {

	/*
	 * Here is where we need serialVersionUID. It is used during deserialization to
	 * verify that the sender ( the person who serializes) and receiver ( the person
	 * who deserializes) of a serialized object have loaded classes for that object
	 * that are compatible with respect to serialization. In case a receiver has
	 * loaded a class for the object that has a different serialVersionUID than that
	 * used to serialize, then deserialization will end with InvalidClassException
	 */
	private static final long serialVersionUID = -5354409965288588299L;
	private long id;
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	// In DB, we are gonna store the encrypted password for security
	private String encryptedPassword;
	private String emailVerificationToken;
	private String emailVerificationStatus;
	private LocalDate date;
	private List<AddressDTO> addresses;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}

	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = emailVerificationToken;
	}

	public String getEmailVerificationStatus() {
		return emailVerificationStatus;
	}

	public void setEmailVerificationStatus(String emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
	}

	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
