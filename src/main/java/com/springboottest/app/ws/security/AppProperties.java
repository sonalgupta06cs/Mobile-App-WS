/**
 * 
 */
package com.appsdevelopeblog.app.ws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 
 * It is a class file to read the key-values from the properties file
 * @author SonaSach
 */
@Component
public class AppProperties {

	@Autowired
	private Environment env;
	
	/**
	 * Get the Token Secret
	 * @return
	 */
	public String getTokenSecret (){
		return env.getProperty("token.secret");
	}
	
}
