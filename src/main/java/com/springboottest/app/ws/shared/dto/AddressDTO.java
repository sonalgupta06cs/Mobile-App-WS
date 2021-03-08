/**
 * 
 */
package com.springboottest.app.ws.shared.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SonaSach
 *
 */
@Getter
@Setter
public class AddressDTO {

	private long id;
	private String addressId;
	private String city;
	private String country;
	private String streetName;
	private String postalCode;
	private String type;

	// Need it so that we can access UserDetails from an Address oject, since we are
	// creating bi-directional
	// relationship, if we get address object, we can get user details and
	// vice-versa.
	private UserDto userDetailss;

}
