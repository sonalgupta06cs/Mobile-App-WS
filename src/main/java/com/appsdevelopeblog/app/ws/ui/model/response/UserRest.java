/**
 * 
 */
package com.appsdevelopeblog.app.ws.ui.model.response;

import java.util.List;

/**
 * @author SonaSach
 *
 * This class would return user public id, first name, last name, email
 * of user that has been just created in. This class would return jspn
 * response back to client with some additional info.
 */
public class UserRest {

	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private List<AddressesRest> addresses;

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

	public List<AddressesRest> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressesRest> addresses) {
		this.addresses = addresses;
	}

}
