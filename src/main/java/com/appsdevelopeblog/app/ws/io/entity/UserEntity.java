package com.appsdevelopeblog.app.ws.io.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cascade;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author SonaSach
 *
 *         This class will be persisted into the data base which means we have
 *         to annotate this class as @Entity This will have the fields which are
 *         going to be stored into the database.
 */
// @Entity:- Give it a name which will be the name of the table that will be
// created to store the user records in DB.
@Entity(name="Users")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = -2384806839807448618L;

	// primary key, auto-incremented and assigned by 1 by framework
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "user_Sequence")
	@SequenceGenerator(name = "user_Sequence", sequenceName = "USER_SEQ")
	/*@GeneratedValue(strategy=GenerationType.AUTO)*/
	private long id;

	// This field is to hold the alphanumeric public user id which we will be
	// sedning back to mobile appkication with a response.
	// which is safe to pass around the network in our HTTP response.It is a
	// mandatory field since we don't want our db that doesnot have
	// alphanumeric id which identifies a record
	@Column(nullable = false)
	private String userId;

	@Column(nullable = false, length = 50)
	private String firstName;

	@Column(nullable = false, length = 50)
	private String lastName;

	@Column(nullable = false, length = 120)
	private String email;
	
	@Column(nullable = false)
	private String password;

	// In DB, we are gonna store the encrypted password for security
	@Column(nullable = false)
	private String encryptedPassword;

	private String emailVerificationToken;

	@Column(nullable = false)//, columnDefinition = "boolean default false")
	//@org.hibernate.annotations.ColumnDefault("false")
	private Boolean emailVerificationStatus;
	
	@OneToMany(mappedBy="userDetails", cascade=CascadeType.ALL)
	private List<AddressEntity> addresses;

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

	public Boolean getEmailVerificationStatus() {
		return emailVerificationStatus;
	}

	public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
	}

	public List<AddressEntity> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressEntity> addresses) {
		this.addresses = addresses;
	}

		
}
