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

import lombok.Getter;
import lombok.Setter;

/**
 * @author SonaSach
 *
 *         This class will be persisted into the data base which means we have
 *         to annotate this class as @Entity This will have the fields which are
 *         going to be stored into the database.
 */
// @Entity:- Give it a name which will be the name of the table that will be
// created to store the user records in DB.
@Setter
@Getter
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

	@Column
	private String emailVerificationToken;

	@Column(nullable = false)//, columnDefinition = "boolean default false")
	//@org.hibernate.annotations.ColumnDefault("false")
	private Boolean emailVerificationStatus;
	
	@OneToMany(mappedBy="userDetails", cascade=CascadeType.ALL)
	private List<AddressEntity> addresses;
}
