/**
 * 
 */
package com.springboottest.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

/**
 * When HTTP PUT request is sent, our authorizationFilter will be able to
 * authorize this user using the Access Token which is sent as part of headers.
 * And if the authorization is successful, it will use the user id to perform
 * that operation that we have requested which is to update user details.
 * Implementing a user authorization filter, a filter that will read the header
 * value and will extract the JSON web token and will perform user authorization
 * using these JSON web token.
 *
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	// Since, we have extended basic authentication filter this is the method that
	// will need to overwrite
	/*
	 * now when the request is made and this filter is triggered, this method will
	 * be called and because it accepts httprequest as an argument. We are able to
	 * access the request headers and the name of the header, We are taking from the
	 * security constants class which we have created. we are reading the
	 * authorization header and when the header is read, it's entire value will be
	 * read from the authorization header. So once we have header, we're checking if
	 * the header is not null. And if it doesnot start with token prefix which is
	 * bearer. the prefix value from the security Constans ringing is the token
	 * prefix beer.
	 * 
	 * if it doesn't start with bearer, we're simply continuing to the next filter
	 * that we have in the chain. But if the token is there and starts with the
	 * bearer then we will need to get UsernamePasswordAuthenticationToken object
	 * and for that we'll create a separate function once we have
	 * that object we will set it as authentication to the SecurityContextHolder and will continue
	 * to the next filter that we have in the chain.
	 * 
	 * Now, this filter needs to be added to WebSecurity configure method.
	 * 
	 * 
	 * Request
	 * http://localhost:8889/users/XgjacKTAPtXE0YBuwpvJfI49djH21A
	 * 
	 * then send authenticationtoken and then hit send button
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		String header = req.getHeader(SecurityConstants.HEADER_STRING);

		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstants.HEADER_STRING);

		if (token != null) {

			token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

			String user = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
					.getBody().getSubject();

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}

			return null;
		}

		return null;
	}
}
