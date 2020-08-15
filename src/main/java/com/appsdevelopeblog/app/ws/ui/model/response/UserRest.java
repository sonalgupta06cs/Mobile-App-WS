/**
 * 
 */
package com.appsdevelopeblog.app.ws.ui.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author SonaSach
 *
 * This class would return user public id, first name, last name, email
 * of user that has been just created in. This class would return jspn
 * response back to client with some additional info.
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UserRest {

	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private List<AddressesRest> addresses;
	
}
