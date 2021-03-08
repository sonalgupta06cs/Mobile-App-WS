package com.appsdevelopeblog.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appsdevelopeblog.app.ws.shared.dto.UserDto;

/*
 * this will contain list of methods to do the user related operations like getUsers, createUsers, updateUsers, deleteUsers.
 * 
 * */
public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userdto);
	UserDto getUser(String email);
	UserDto getUserByUserId(String userId);
	UserDto updateUser(String id, UserDto userDto);
	void deleteUser(String userId);
	List<UserDto> getUsers(int page, int limit);
	List<UserDto> getAllUsers();
	boolean verifyEmailToken(String token);
	boolean requestPasswordReset(String email);
	boolean resetPassword(String token, String password);
	
}
