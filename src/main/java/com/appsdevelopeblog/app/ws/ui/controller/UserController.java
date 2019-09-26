package com.appsdevelopeblog.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdevelopeblog.app.ws.exceptions.UserServiceExceptions;
import com.appsdevelopeblog.app.ws.service.AddressService;
import com.appsdevelopeblog.app.ws.service.UserService;
import com.appsdevelopeblog.app.ws.shared.dto.AddressDTO;
import com.appsdevelopeblog.app.ws.shared.dto.UserDto;
import com.appsdevelopeblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdevelopeblog.app.ws.ui.model.response.AddressesRest;
import com.appsdevelopeblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdevelopeblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdevelopeblog.app.ws.ui.model.response.RequestOperationName;
import com.appsdevelopeblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdevelopeblog.app.ws.ui.model.response.UserRest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "users") // http://localhost:8888/users
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;

	/*
	 * When Mobile api sends the user information in json or xml format to web
	 * service end point, then web service end point(i.e. our method) would have to
	 * accept this information and convert this json payload into java object.Then,
	 * we can use this java object to persist into the data base. So, Framework
	 * would automatically do this conversion from json to java n java to json
	 * automatically. Simply, create a pojo class with those incoming fields in
	 * json.
	 * 
	 * This method would be triggered once it receives the HTTP POST request.
	 * 
	 * In order to be able to read the body from the HTTP Request and then convert
	 * that json to java object, we need to use @RequestBody Annotation n we will
	 * have to pass a class which will be used to create the java object out of this
	 * request body.
	 * 
	 * Once user details are successfully stored into the database, we will need to
	 * respond back to mobile application(calling client) with some information that
	 * confirms that user details have been successfully stored. This means we will
	 * need to change the String return type to a different java object, which will
	 * contain little more information than plain string text.
	 * 
	 * public String createUser(@RequestBody UserDetailsRequestModel userDetails) {
	 * 
	 * 
	 * Now lets try sending this application an HTTP POST request and see if we are
	 * able to store the details sucessfully.
	 * 
	 * Different Media Type Support:- When a POST request is made to return the user
	 * details response post creation, if you want to return in XML or JSON
	 * representation depending on what calling client has requested in "Accept"
	 * Header, add support in @PostMapping. to support the response in xml or json
	 * format. add "produces=MediaType.APPLICATION_XML_VALUE or JSON_VALUE"
	 * to @PostMapping so that this end point response can be produced in XML or
	 * JSON format.
	 * 
	 * also when it receives the request from the calling client to create the
	 * users, if calling client has sent "content_type" as xml, that means our end
	 * point should be able to consume the request in xml format, for that to
	 * happen, we should add "consumes=MediaType.xml or json" in @PostMapping.
	 * 
	 * @Produces defines the media types that the methods of a resource class or
	 * MessageBodyReader can produce. If not specified, a container will assume that
	 * any media type can be produced.
	 * 
	 * @Consumes defines the media types that the methods of a resource class or
	 * MessageBodyReader can accept. If not specified, a container will assume that
	 * any media type is acceptable.
	 * 
	 * but when specidfied, it can only consume or produce the ones specified.
	 */
	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		UserRest userResponse = new UserRest();

		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		//UserDto userDto = new UserDto();
		// we have to populate userdto object with info received from request body to
		// userdto object, for that we are using BeanUtils class.
		// BeanUtils does shallow object mapping, so lets try with ModelMapper.
		//BeanUtils.copyProperties(userDetailsRequest, userDto);
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		//BeanUtils.copyProperties(createdUser, userResponse);
		userResponse = modelMapper.map(createdUser, UserRest.class);

		return userResponse;

	}

	/*
	 * We will make this method to respond to GET Request We will bind this method
	 * to HTTP GET request, Use @GetMapping Annotation for that. This method will
	 * need to accept a user id which will be used to query the database and which
	 * will then return JSON payload with the user details
	 * 
	 * Different Media Type Support:- When a GET request is made to return the user
	 * details, if you want to return in XML or JSON representation depending on
	 * what calling client has requested in "Accept" Header, Spring framework
	 * provides a very good support, just add 1 dependency in pom.xml
	 * "jackson-dataformat-xml", post that add support in @GetMapping
	 * and @PostMapping. to support the response in xml or json format. add
	 * "produces=MediaType.APPLICATION_XML_VALUE" to GetMapping so that this end
	 * point response can be produced in XML or JSON format.
	 * 
	 */
	@ApiOperation( value="The Get Usr Details Web Service end point ",
			       notes="${userController.getUser.ApiOperation.notes}"
			     )
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") })
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) { // http://localhost:8888/users/{id} - GET Method


		UserDto userdto = userService.getUserByUserId(id);
		//BeanUtils.copyProperties(userdto, userResponse);
		ModelMapper mapper = new ModelMapper();
		UserRest userResponse = mapper.map(userdto, UserRest.class);

		return userResponse;

	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") })
	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public UserRest updateUser(@RequestBody UserDetailsRequestModel requestModel, @PathVariable String id) {

		
		ModelMapper mapper = new ModelMapper();
		
		UserDto userDto = mapper.map(requestModel, UserDto.class);

		UserDto updatedUser = userService.updateUser(id, userDto);
		
		UserRest userResponse = mapper.map(updatedUser, UserRest.class);

		return userResponse;

	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") })
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {

		OperationStatusModel statusModel = new OperationStatusModel();
		statusModel.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		statusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());

		return statusModel;

	}

	// Method to handle the pagination call to limit the users retrieved from the db
	@ApiImplicitParams({
			@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header") })
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUserPagination(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {

		List<UserRest> userResponseList = new ArrayList<>();

		List<UserDto> userdtoList = userService.getUsers(page, limit);

		for (UserDto user : userdtoList) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(user, userModel);
			userResponseList.add(userModel);
		}

		return userResponseList;

	}
	
	
	// http://localhost:8889/mobile-app-ws/users/alhGELXjmNfQ9bBCZKnJKRp4ZlAjfg/addresses
	@GetMapping(path = "/{userId}/addresses", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<AddressesRest> getUserAddressesByUserId(@PathVariable String userId) { // http://localhost:8888/users/{id} - GET Method

		List<AddressesRest> addressesRest = new ArrayList<>();
		
		List<AddressDTO> addressesdto = addressService.getUserAddressesByUserId(userId);
		
		if(addressesdto != null && !addressesdto.isEmpty()) {
			Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			addressesRest = new ModelMapper().map(addressesdto, listType);
		}
		
		
		return addressesRest;

	}
	
	// http://localhost:8889/mobile-app-ws/users/wAKA4OZqaywiiZklKYo2inGalCiS5Q/addresses/Gz5Xxa7qgI0pdXokv54iV7O5TXIesu
	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public AddressesRest getUserAddressByAddId(@PathVariable String userId, @PathVariable String addressId) { // http://localhost:8888/users/{id} - GET Method

		
		AddressDTO addressesdto = addressService.getUserAddressByAddId(addressId);
		
		ModelMapper mapper = new ModelMapper();		
		
		return mapper.map(addressesdto, AddressesRest.class);

	}
	
	
	/**
	 * Get All the users
	 * 
	 * @param id
	 * @return
	 */
	/*
	 * @GetMapping(produces= { MediaType.APPLICATION_XML_VALUE,
	 * MediaType.APPLICATION_JSON_VALUE }) public List<UserRest> getAllUsers() {
	 * 
	 * List<UserRest> userResponseList = new ArrayList<>();
	 * 
	 * List<UserDto> userdtoList = userService.getAllUsers();
	 * 
	 * for(UserDto user : userdtoList) { UserRest userModel = new UserRest();
	 * BeanUtils.copyProperties(user, userModel); userResponseList.add(userModel); }
	 * 
	 * return userResponseList;
	 * 
	 * }
	 */

}
