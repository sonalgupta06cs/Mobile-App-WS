/**
 * 
 */
package com.springboottest.app.ws.security;

import com.springboottest.app.ws.SpringApplicationContext;

/**
 * @author SonaSach
 *
 *         We are about to start working with the authentications filter which
 *         will be used to authenticate user with the profile username and
 *         password.
 * 
 *         When the client will send a http post request with the details like
 *         username and password and Spring framework will use this username and
 *         password and sends those details to Authentication Filter to
 *         authenticate the request.
 * 
 *         And if authentication is successful, our authentication filter will
 *         issue access token and that has authentication filter uses some
 *         constants like Token expiration time, token prefix header and Sign-Up
 *         URL.
 * 
 *         So, Here, we're going to create a separate class which will hold
 *         those details.
 */
public class SecurityConstants {

	/*
	 * So the very first value token expiration time will be used when we generate
	 * token and this token will be valid for 10 days. You can change this for one
	 * day or two days it's up to you. And this value is going to be milliseconds.
	 */
	public static final long EXPIRATION_TIME = 864000000; // 10 days in ms

	/*
	 * Then we have a token prefix and the prefix is going to be passed on together
	 * with the headers String in HTTP request.
	 * 
	 * So your HTTP POST request will have an authorization header and that header
	 * will contain bearer which has a string value And then after the bearer, you
	 * provide a token value. So the header string is authorization 
	 */
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	/*Signup URL is public and is used to configure the public access */
	public static final String SIGN_UP_URL = "/users";
	// So it must be a unique stream of characters that will be used to sign the json web token.
	//public static final String TOKEN_SECRET = "jf9i4jgu83nf10";
	// And then we have the token secret and this value will be used in the
	// encryption of the value of our access token.
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
    public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request";
    public static final String PASSWORD_RESET_URL = "/users/password-reset";
    public static final String H2_CONSOLE = "/h2-console/**";
	
	
	/**
	 * Method to get the Token Secret from the properties file
	 * @return
	 */
	public static String getTokenSecret (){
		AppProperties appProp = (AppProperties) SpringApplicationContext.getBean("appProperties");
		return appProp.getTokenSecret();
	}

}
