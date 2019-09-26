package com.appsdevelopeblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdevelopeblog.app.ws.entity.repositry.UserRepository;
import com.appsdevelopeblog.app.ws.exceptions.UserServiceExceptions;
import com.appsdevelopeblog.app.ws.io.entity.UserEntity;
import com.appsdevelopeblog.app.ws.service.UserService;
import com.appsdevelopeblog.app.ws.shared.Utils;
import com.appsdevelopeblog.app.ws.shared.dto.AddressDTO;
import com.appsdevelopeblog.app.ws.shared.dto.UserDto;
import com.appsdevelopeblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null)
			throw new RuntimeException("User already exists");
		
		    for (int i = 0; i < user.getAddresses().size(); i++) {
				AddressDTO addr = user.getAddresses().get(i);
				addr.setUserDetails(user);
				addr.setAddressId(utils.generateAddressId(30));
				user.getAddresses().set(i, addr);
			}
		
			//BeanUtils.copyProperties(user, userEntity);
			ModelMapper mapper = new ModelMapper();
			UserEntity userEntity  = mapper.map(user, UserEntity.class);

			String publicUserId = utils.generateUserId(30);
			userEntity.setUserId(publicUserId);
			userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			userEntity.setEmailVerificationStatus(false);

			UserEntity storedUserResponse = userRepository.save(userEntity);

			//BeanUtils.copyProperties(storedUserResponse, userResponse);
			 UserDto userResponse = mapper.map(storedUserResponse, UserDto.class);

			return userResponse;
		}
	
	@Override
	public UserDto getUser(String email) {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null)
			throw new UsernameNotFoundException("email/username " + email);
		
		UserDto userResponse = new UserDto();
		BeanUtils.copyProperties(userEntity, userResponse);
		
		return userResponse;
	}

	/*
	 * So now we have a method which again is being used by a spring framework.
	 * It's called loadUserByUserName and that will help spring framework to load
	 * user details when it needs.
	 * 
	 * And that method will be used in the process of user sign in our mobile
	 * application where the client will send a http post request with the details
	 * like username and password and Spring framework will use this username and
	 * password to see if it can find that user in our database, if it finds, it will return that user details.
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserEntity userResponse = userRepository.findByEmail(email);
		if (userResponse == null)
			throw new UsernameNotFoundException("email/username " + email);

		return new User(userResponse.getEmail(), userResponse.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		
		 UserDto userdto = new UserDto();
		
		 UserEntity userEntity =  userRepository.findUserByUserId(userId);
		 if(userEntity == null) throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage() + userId);
			 
		 BeanUtils.copyProperties(userEntity, userdto);
		 return userdto;  
		
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		
		UserDto userFinalResponse = new UserDto();
		
		UserEntity userEntity =  userRepository.findUserByUserId(userId);
		if(userEntity == null) throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage() + userId);
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		
		UserEntity updatedUserResponse = userRepository.save(userEntity);
		
		BeanUtils.copyProperties(updatedUserResponse, userFinalResponse);

		return userFinalResponse;
	}

	@Override
	public void deleteUser(String userId) {
		
		UserEntity userEntity =  userRepository.findUserByUserId(userId);
		if(userEntity == null) throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage() + userId);
		
		userRepository.delete(userEntity);
		
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		
		List<UserDto> userDtoList = new ArrayList<>();
		
		if(page>0) page = page-1;
		
		Pageable pageable = PageRequest.of(page, limit);
		Page<UserEntity> usersPage = userRepository.findAll(pageable);
		
		List<UserEntity> usersEntity = usersPage.getContent();
		
		for(UserEntity user : usersEntity) {
			UserDto userModel = new UserDto();
			BeanUtils.copyProperties(user, userModel);
			userDtoList.add(userModel);
		}
		
		return userDtoList;
	}

	@Override
	public List<UserDto> getAllUsers() {
		
		List<UserDto> userDtoList = new ArrayList<>();
		
		Iterable<UserEntity> usersFindAll = userRepository.findAll(Sort.unsorted());
		
		for(UserEntity usersEntity : usersFindAll) {
			
			UserDto userModel = new UserDto();
			BeanUtils.copyProperties(usersEntity, userModel);
			userDtoList.add(userModel);
			
		}
		
		return userDtoList;
	}
}
