package com.appsdevelopeblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appsdevelopeblog.app.ws.entity.repositry.PasswordResetTokenRepository;
import com.appsdevelopeblog.app.ws.entity.repositry.UserRepository;
import com.appsdevelopeblog.app.ws.exceptions.UserServiceExceptions;
import com.appsdevelopeblog.app.ws.io.entity.PasswordResetTokenEntity;
import com.appsdevelopeblog.app.ws.io.entity.UserEntity;
import com.appsdevelopeblog.app.ws.service.UserService;
import com.appsdevelopeblog.app.ws.shared.AmazonSES;
import com.appsdevelopeblog.app.ws.shared.Utils;
import com.appsdevelopeblog.app.ws.shared.dto.AddressDTO;
import com.appsdevelopeblog.app.ws.shared.dto.UserDto;
import com.appsdevelopeblog.app.ws.ui.mapper.UserDtoToUserRestMapper;
import com.appsdevelopeblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	AmazonSES amazonSES;

	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null)
			throw new UserServiceExceptions("Record already exists");

		for (int i = 0; i < user.getAddressesDto().size(); i++) {
			AddressDTO address = user.getAddressesDto().get(i);
			address.setUserDetailss(user);
			address.setAddressId(utils.generateAddressId(30));
			user.getAddressesDto().set(i, address);
		}

		// BeanUtils.copyProperties(user, userEntity);
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);

		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
		userEntity.setEmailVerificationStatus(Boolean.FALSE);

		UserEntity storedUserDetails = userRepository.save(userEntity);

		// BeanUtils.copyProperties(storedUserDetails, returnValue);
		UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

		// Send an email message to user to verify their email address
		amazonSES.verifyEmail(returnValue);

		return returnValue;
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
	 * So now we have a method which again is being used by a spring framework. It's
	 * called loadUserByUserName and that will help spring framework to load user
	 * details when it needs.
	 * 
	 * And that method will be used in the process of user sign in our mobile
	 * application where the client will send a http post request with the details
	 * like username and password and Spring framework will use this username and
	 * password to see if it can find that user in our database, if it finds, it
	 * will return that user details.
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserEntity userResponse = userRepository.findByEmail(email);
		if (userResponse == null)
			throw new UsernameNotFoundException("email/username " + email);

		// return new User(userResponse.getEmail(), userResponse.getEncryptedPassword(),
		// userResponse.getEmailVerificationStatus(), true, true,
		// true, new ArrayList<>());
		return new User(userResponse.getEmail(), userResponse.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUserByUserId(String userId) {

		UserDto userdto = null;

		UserEntity userEntity = userRepository.findUserByUserId(userId);
		if (userEntity == null)
			throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage() + userId);

		userdto = UserDtoToUserRestMapper.INSTANCE.toUserDto(userEntity);

		return userdto;

	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {

		UserDto userFinalResponse = new UserDto();

		UserEntity userEntity = userRepository.findUserByUserId(userId);
		if (userEntity == null)
			throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage() + userId);

		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());

		UserEntity updatedUserResponse = userRepository.save(userEntity);

		BeanUtils.copyProperties(updatedUserResponse, userFinalResponse);

		return userFinalResponse;
	}

	@Transactional
	@Override
	public void deleteUser(String userId) {

		UserEntity userEntity = userRepository.findUserByUserId(userId);
		if (userEntity == null)
			throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage() + userId);

		userRepository.delete(userEntity);

	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {

		List<UserDto> returnValue = new ArrayList<>();

		if (page > 0)
			page = page - 1;

		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();

		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}

		return returnValue;
	}

	@Override
	public List<UserDto> getAllUsers() {

		List<UserDto> userDtoList = new ArrayList<>();

		List<UserEntity> usersRetrievedList = (List<UserEntity>) userRepository.findAll();

		for(UserEntity user : usersRetrievedList) {
			UserDto userModel = new UserDto();
		 	BeanUtils.copyProperties(user, userModel); 
		 	userDtoList.add(userModel); 
		 }

//		List<AddressDTO> addressDTOList = new ArrayList<>();
//		usersRetrievedList.stream().forEach(user -> {
//
//			userDtoList.add(UserDtoToUserRestMapper.INSTANCE.toUserDto(user));
//
//			user.getAddresses().forEach(address -> {
//				addressDTOList.add(UserDtoToUserRestMapper.INSTANCE.toAddressDTO(address));
//			});
//
//		});

		//addressDTOList.forEach(System.out::print);

		return userDtoList;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		boolean returnValue = false;

		// Find user by token
		UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

		if (userEntity != null) {
			boolean hastokenExpired = Utils.hasTokenExpired(token);
			if (!hastokenExpired) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepository.save(userEntity);
				returnValue = true;
			}
		}

		return returnValue;
	}

	@Override
	public boolean requestPasswordReset(String email) {

		boolean returnValue = false;

		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null) {
			return returnValue;
		}

		String token = new Utils().generatePasswordResetToken(userEntity.getUserId());

		PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
		passwordResetTokenEntity.setToken(token);
		passwordResetTokenEntity.setUserDetails(userEntity);
		passwordResetTokenRepository.save(passwordResetTokenEntity);

		returnValue = new AmazonSES().sendPasswordResetRequest(userEntity.getFirstName(), userEntity.getEmail(), token);

		return returnValue;
	}

	@Override
	public boolean resetPassword(String token, String password) {
		boolean returnValue = false;

		if (Utils.hasTokenExpired(token)) {
			return returnValue;
		}

		PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

		if (passwordResetTokenEntity == null) {
			return returnValue;
		}

		// Prepare new password
		String encodedPassword = bCryptPasswordEncoder.encode(password);

		// Update User password in database
		UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
		userEntity.setEncryptedPassword(encodedPassword);
		UserEntity savedUserEntity = userRepository.save(userEntity);

		// Verify if password was saved successfully
		if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
			returnValue = true;
		}

		// Remove Password Reset token from database
		passwordResetTokenRepository.delete(passwordResetTokenEntity);

		return returnValue;
	}
}
