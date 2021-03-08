/**
 * 
 */
package com.springboottest.app.ws.ui.model.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SonaSach
 *
 * Common response class for sending API response to UI
 */
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class ApiResponse<T> implements Serializable {

	private static final long serialVersionUID = 835521240997340276L;

	private boolean success;
	private T data;
	private Object error;
	private Object uiPageJson;

	public ApiResponse() {

	}

	public ApiResponse(boolean success, T data, Object error) {
		super();
		this.success = success;
		this.data = data;
		this.error = error;
	}
	
	public void setData(T data) {
		this.data = data;
	}

}
