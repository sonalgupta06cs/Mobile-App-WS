package com.appsdevelopeblog.app.ws.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.appsdevelopeblog.app.ws.ui.model.response.CustomErrorJSONMessage;

/**
 * CLass to handle the exceptions thrown
 * 
 * @author SonaSach
 *
 */
@ControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(value= {UserServiceExceptions.class})
	public ResponseEntity<Object> handleUserServiceException(UserServiceExceptions ex, WebRequest request){
		
		// Instead of returning UserServiceException class, We will return our custom which will be used  
		// and converted in JSON or xml format when we will have some exception thrown.
		CustomErrorJSONMessage customErrorJSONMessage = new CustomErrorJSONMessage(new Date(), ex.getMessage());
		
		return new ResponseEntity<>(customErrorJSONMessage, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR);
		// return new ResponseEntity<>(ex.getMessage(), HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value= {Exception.class})
	public ResponseEntity<Object> handleOthersException(Exception ex, WebRequest request){

		CustomErrorJSONMessage customErrorJSONMessage = new CustomErrorJSONMessage(new Date(), ex.getMessage());
		
		return new ResponseEntity<>(customErrorJSONMessage, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
