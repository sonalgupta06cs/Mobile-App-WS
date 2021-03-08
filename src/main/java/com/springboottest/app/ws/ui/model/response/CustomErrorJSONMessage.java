/**
 * 
 */
package com.appsdevelopeblog.app.ws.ui.model.response;

import java.util.Date;

/**
 * 
 * This Class is created to have custom json error message.
 * This class will be used and converted in JSON when we will have some exception thrown.
 * 
 * @author SonaSach
 *
 */
public class CustomErrorJSONMessage {

	private Date timestamp;
	private String message;
	
	public CustomErrorJSONMessage() {}

	public CustomErrorJSONMessage(Date timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
