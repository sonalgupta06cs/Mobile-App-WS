/**
 * 
 */
package com.appsdevelopeblog.app.ws.config.profiles;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author SonaSach
 *
 */
@Profile("dev")
@Configuration
public class JavaDevConfig {
	
	@PostConstruct
	public void env() {
		System.out.println("dev profile loaded");
	}

}
