/**
 * 
 */
package com.springboottest.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboottest.app.ws.SpringApplicationContext;
import com.springboottest.app.ws.service.UserService;
import com.springboottest.app.ws.shared.dto.UserDto;
import com.springboottest.app.ws.ui.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author SonaSach
 *
 *         This class will be used when an HTTP POST request is sent for user to
 *         sign in to our application using email and password.
 * 
 *         Authentication means confirming your own identity, whereas
 *         authorization means being allowed access to the system. In even more
 *         simpler terms authentication is the process of verifying oneself,
 *         while authorization is the process of verifying what you have access
 *         to.
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/*
	 * Request:- { "email":"sachgupta251087@gmail.com", "password":"password@123" }
	 * 
	 * Response:- authorization →Bearer eyJhbGciOiJIUzUxMiJ9.
	 * eyJzdWIiOiJzYWNoZ3VwdGEyNTEwODdAZ21haWwuY29tIiwiZXhwIjoxNTU0Nzk1MDQ5fQ.
	 * kPZIBL5JIwRxdV99cHRyaUF3Rtj0OJ3LzbTJAbuBgf_TpXw0YFLS7vG5o2lta8g8miqK2PBiV6undOJDq8qkdg
	 * cache-control →no-cache, no-store, max-age=0, must-revalidate content-length
	 * →0 date →Sat, 30 Mar 2019 07:30:50 GMT expires →0 pragma →no-cache userid
	 * →XgjacKTAPtXE0YBuwpvJfI49djH21A
	 * 
	 * 
	 * This method will be triggered when user has passed username and password to
	 * authenticate himself OK so what's going to happen when our web services
	 * receive a request to authenticate user, Spring Framework will be used to
	 * authenticate our user with the username and password that were provided by
	 * the user and attemptAuthentication method will be triggered and the Json
	 * payload that was included in the request body will be used to create
	 * UserLoginRequestModel Java class which we have created.
	 * 
	 * It contains email and password and email has been used as a username in our
	 * case. Next, it will use authentication manager which comes from Spring.
	 * 
	 * We have declared that here and it is being initialized in our constructor.So,
	 * our authentication manager will be used to authenticate user and it will use
	 * email and password which are being read from the UserLoginRequest model
	 * class.
	 * 
	 * Now the Spring framework will do all the work that it needs.
	 * 
	 * It will look up user in our database and for that we have implemented a
	 * method loadUserByUsername
	 * 
	 * it will find the user and it will authenticate the user using username and
	 * password and if username and password matched the record that we have a new
	 * method that will be triggered.
	 * 
	 * FLow:- WebSecurity configure(HttpSecurity http) -> getAuthenticationFilter()
	 * -> attemptAuthentication() -> loadUserByUsername -> if successful ->
	 * successfulAuthentication()
	 * 
	 * 
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {

			UserLoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(),
					UserLoginRequestModel.class);

			// on call of below method, it makes a call to loadByUserName to validate from
			// the db
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

		} catch (IOException e) {

			throw new RuntimeException(e);

		}

	}

	/*
	 * It's called successfulAuthentication.
	 * 
	 * So once our request continues and password is successfully authenticated then
	 * , this successfulAuthentication will be called. If username or password are
	 * not correct then this meant that successfulAuthentication will not be called.
	 * 
	 * So once they authentication is successful this method will be called by the
	 * spring framework.
	 * 
	 * And the user name will be read from the authentication object. Then, we'll
	 * use JSON token library that we have included in our pom.xml.
	 * 
	 * The compact representation of a signed JWT is a string that has three parts,
	 * each separated by a .: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.
	 * ipevRNuRP6HflG8cFKnmUPtypruRC4fb1DWtoLL62SY
	 * 
	 * Each part is Base64URL-encoded. The first part is the header, which at a
	 * minimum needs to specify the algorithm used to sign the JWT. The second part
	 * is the body. This part has all the claims of this JWT encoded in it. The
	 * final part is the signature. It's computed by passing a combination of the
	 * header and body through the algorithm specified in the header. If you pass
	 * the first two parts through a base 64 url decoder, you'll get the following
	 * (formatting added for clarity):
	 * 
	 * header
	 * 
	 * { "alg": "HS256" } body
	 * 
	 * { "sub": "Joe" } In this case, the information we have is that the HMAC using
	 * SHA-256 algorithm was used to sign the JWT. And, the body has a single claim,
	 * sub with value Joe. There are a number of standard claims, called Registered
	 * Claims, in the specification and sub (for subject) is one of them. To compute
	 * the signature, you need a secret key to sign it.
	 * 
	 * this json token will then be used in "Authorizationfilter" once it is
	 * generated. It will be included into the header and the client that receives
	 * this response will need to extract this token and it will need to store it
	 * and it has to be stroed in key chain, then every time the mobile application
	 * will need to communicate with protected resources, it will have to send this
	 * For example Get list of all users or update user details or delete user
	 * details when it needs to communicate with our API. It will need to include
	 * this json web token as a header into the request otherwise the request
	 * willn't be authorized.
	 */
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String userName = ((User) auth.getPrincipal()).getUsername();

		// we are building this json web token here,we token will contain the username,
		// expiration time, then we are signing this
		// with HS512 algorithm and for signature, we are again using token secret
		String token = Jwts.builder().setSubject(userName)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();

		UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
		UserDto userDto = userService.getUser(userName);

		// once jwt is generated, it will be added as a header information.
		// So our header will contain the token that we have just generated and how to
		// view this token details in the response.
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		// the last step is to take this authentication filter and add it to
		// websecurity.

		// add public user id in response
		response.addHeader("UserID", userDto.getUserId());

	}

}
