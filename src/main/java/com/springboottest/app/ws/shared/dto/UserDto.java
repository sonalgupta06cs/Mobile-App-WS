/**
 * 
 */
package com.appsdevelopeblog.app.ws.shared.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

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
@Setter
@Getter
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
	private Boolean emailVerificationStatus = false;
	private LocalDate date;
	private List<AddressDTO> addressesDto;
}
