/**
 * 
 */
package com.appsdevelopeblog.app.ws.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdevelopeblog.app.ws.service.UserService;

/**
 * @author SonaSach
 *
 *         This class will define at least one meant that like sign up method
 *         end-point that users can use to create a new account and that we will
 *         make as public and all other web service end-points will be protected
 *         and require authorization and authentication.
 */
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final UserService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	/*
	 * we are overriding couple of methods since we are extending security configure
	 * adapter and the first method which is called configure and it takes in
	 * security as a method argument is needed to configure some of the web series
	 * entry points in our application as public and some of them as protected.
	 * 
	 * for example in this example I'm configuring HTTP POST request which will be
	 * sent to sign up url i.e. /users end-point hardcoded.
	 * 
	 * So Any post request it's sent to forward slash users and we know that if we
	 * send the post request to this web service end-point, a code will be triggered
	 * to create a new user account with our application. So this request should be
	 * authorized We will permit this operation and we use permits all for that. So,
	 * any other request will need to be authenticated.
	 * 
	 * we will have a filter which is for authenticating our user.
	 * So that is going to be your authenticationfilter that we have just created and also will need to give
	 * authenticationmanager as an argument and now this will participate in the security and when
	 * the request is sent to authenticate user it will be used by Spring Framework.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests()
		        .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
		        .permitAll()
		        .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
		        .permitAll()
		        .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**", "/actuator/**") // enable actuator path as well
		        .permitAll().anyRequest()
				.authenticated().and()
				.addFilter(getAuthenticationFilter())
				.addFilter(new AuthorizationFilter(authenticationManager()))
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}

	/*
	 * And the second Method we are saying that authentication manager builder, the
	 * user details interface that we will use and we are also having a
	 * "bCryptPasswordEncoder" encryption method that we use to protect user
	 * password.
	 * 
	 * Again we are just taking advantage of Spring's security framework.
	 * 
	 * We're not going to create a authentication manager builder ourselves.
	 * 
	 * It all comes from Spring security and many things are being done for us under
	 * the hood or we need to do is to tell which interface we are using for user
	 * detail's service and what encryption method we use to protect our password.
	 * And then encryption method we use also comes from spring.
	 * 
	 * Now, we should be able to run our application.
	 * 
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);

	}

	/*
	 * we have provided this authentication manager builder with the user details
	 * service interface and this interface helps Spring framework to load user
	 * details from our database and to help load user details from the database.
	 * 
	 * We've also provided the password encoder that we use to encode user provided
	 * password and the password encoded class is called Decrypt password encoder.
	 * 
	 * So now because we have given authentication manager builder the user details
	 * service which is our user service interface and we made that userService
	 * interface extend UserDetailsService that comes from Spring security framework
	 * we are importing here.
	 * 
	 * 
	 * In UserService Interface:-
	 * 
	 * We will need to provide implementation of one of the methods that these
	 * UserDetailsService interface defines.
	 * 
	 * So if I go to a UserServiceImpl implementation class, our UserServiceImpl
	 * implementation class implements UserService interface and because it
	 * implements UserService interface and UserService interface extends
	 * UserDetailedService from spring So, We will need to provide implementation of
	 * loadUserByUsername and by implementing this method which will help spring
	 * framework to load user details from our database by username and in the case
	 * of our application the username is email address.
	 */
	
	/**
	 * Method to create a custom authentication url(/users/login) to get rid of what spring provides(/login)
	 * Once, you have this function you can replace "new AuthenticationFilter(authenticationManager())" in configure method
	 * .addFilter(new AuthenticationFilter(authenticationManager()));
	 * with the below function.
	 * @return AuthenticationFilter
	 * @throws Exception
	 */
	public AuthenticationFilter getAuthenticationFilter() throws Exception {
		
		final AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager());
		authenticationFilter.setFilterProcessesUrl("/users/login");
		
		return authenticationFilter;
		
	}

}
