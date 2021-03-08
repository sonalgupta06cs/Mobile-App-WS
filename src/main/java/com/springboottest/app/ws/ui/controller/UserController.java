package com.springboottest.app.ws.ui.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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

import com.springboottest.app.ws.exceptions.UserServiceExceptions;
import com.springboottest.app.ws.service.AddressService;
import com.springboottest.app.ws.service.UserService;
import com.springboottest.app.ws.shared.dto.AddressDTO;
import com.springboottest.app.ws.shared.dto.UserDto;
import com.springboottest.app.ws.ui.mapper.UserDtoToUserRestMapper;
import com.springboottest.app.ws.ui.model.request.PasswordResetModel;
import com.springboottest.app.ws.ui.model.request.PasswordResetRequestModel;
import com.springboottest.app.ws.ui.model.request.UserDetailsRequestModel;
import com.springboottest.app.ws.ui.model.response.AddressesRest;
import com.springboottest.app.ws.ui.model.response.ErrorMessages;
import com.springboottest.app.ws.ui.model.response.OperationStatusModel;
import com.springboottest.app.ws.ui.model.response.RequestOperationName;
import com.springboottest.app.ws.ui.model.response.RequestOperationStatus;
import com.springboottest.app.ws.ui.model.response.UserRest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users") // http://localhost:8888/users
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;

	/*
	 * When Mobile api sends the user information in json or xml format to web
	 * service end point, then web service end point(i.e. our method) would have to
	 * accept this information and convert this json payload into java object.Then,
	 * we can use this java object to persist into the data base. So, Framework
	 * would automatically do this conversion from son to java n java to json
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

		try {
			UserRest userResponse = new UserRest();
	
			if (userDetails.getFirstName().isEmpty())
				throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
			
			UserDto userDto = UserDtoToUserRestMapper.INSTANCE.toUserDTOFromUserModel(userDetails);
	
			UserDto createdUser = userService.createUser(userDto);
			userResponse = UserDtoToUserRestMapper.INSTANCE.toUserRest(createdUser);
	
			return userResponse;
		} catch(Exception e) {
			log.error("Exception:{}", e.getMessage());
			return null;
		}

	}
	
	//TODO: - New- Send email to a user saying that email() has been created.
	@PostMapping(path="/sendEmail")
	public String sendEmail(@RequestBody String email) {
		
		
		return email;		
	}
	
	// New- Get all the users
	@GetMapping(path="/getAllUsers", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getAllUsers() {

		List<UserDto> userdtoList = userService.getAllUsers();		
		log.info("UsersRetrieved:{}", userdtoList.size());
		
		List<UserRest> userResponseList = UserDtoToUserRestMapper.INSTANCE.toUserRest(userdtoList);
		int searchCount = CollectionUtils.isNotEmpty(userResponseList) ? userResponseList.size() : 0 ;
		log.info("Total elements searched:{}", searchCount);

	    return userResponseList;
	}
	
	//TODO: - New- Download all the users in an excel
	@PostMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest downloadAllUsersListinExcel(@PathVariable String id) {


		UserDto userdto = userService.getUserByUserId(id);
		//BeanUtils.copyProperties(userdto, userResponse);
		ModelMapper mapper = new ModelMapper();
		UserRest userResponse = mapper.map(userdto, UserRest.class);

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

		UserRest userResponse = UserDtoToUserRestMapper.INSTANCE.toUserRest(userdto);
		
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
	//@ApiImplicitParams({ @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", 
	//		                               paramType = "header") })
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }) 
	public List<UserRest> getUserPagination(@RequestParam(value = "page", defaultValue = "0") int page,
			                                  @RequestParam(value = "limit", defaultValue = "25") int limit) {
	  
		List<UserDto> userdtoList = userService.getUsers(page, limit);
		  
		List<UserRest> userResponseList = UserDtoToUserRestMapper.INSTANCE.toUserRest(userdtoList);
		int searchCount = CollectionUtils.isNotEmpty(userResponseList) ? userResponseList.size() : 0 ;
		log.info("Total elements retrieved:{}", searchCount);

		return userResponseList;	  
	  }
	 
	
	
	@GetMapping(path = "/{userId}/addresses", produces = { MediaType.APPLICATION_XML_VALUE, 
			                                               MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public Resources<AddressesRest> getUserAddressesByUserId(@PathVariable String userId) {

		List<AddressesRest> addressesListRestModel = new ArrayList<>();
		
		List<AddressDTO> addressesDto = addressService.getUserAddressesByUserId(userId);
		
		if(addressesDto != null && !addressesDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			addressesListRestModel = new ModelMapper().map(addressesDto, listType);
			
			for (AddressesRest addressRest : addressesListRestModel) {
				Link addressLink = linkTo(methodOn(UserController.class).getUserAddressByAddId(userId, addressRest.getAddressId()))
						.withSelfRel();
				addressRest.add(addressLink);

				Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
				addressRest.add(userLink);
			}
		}
		
		return new Resources<>(addressesListRestModel);
	}
	
	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE, 
			                                                           MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public Resource<AddressesRest> getUserAddressByAddId(@PathVariable String userId, @PathVariable String addressId) {

		AddressDTO addressesDto = addressService.getUserAddressByAddId(addressId);
		
		ModelMapper modelMapper = new ModelMapper();		
		Link addressLink = linkTo(methodOn(UserController.class).getUserAddressByAddId(userId, addressId)).withSelfRel();
		Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddressesByUserId(userId)).withRel("addresses");

		AddressesRest addressesRestModel = modelMapper.map(addressesDto, AddressesRest.class);
		addressesRestModel.add(addressLink);
		addressesRestModel.add(userLink);
		addressesRestModel.add(addressesLink);

		return new Resource<>(addressesRestModel);
	}
	
	
	 /*
     * http://localhost:8080/mobile-app-ws/users/email-verification?token=sdfsdf
     * */
    @GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
        
        boolean isVerified = userService.verifyEmailToken(token);
        
        if(isVerified)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }
    
	 /*
     * http://localhost:8080/mobile-app-ws/users/password-reset-request
     * */
    @PostMapping(path = "/password-reset-request", 
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
    	OperationStatusModel returnValue = new OperationStatusModel();
 
        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
        
        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
 
        if(operationResult)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }
    
    
    
    @PostMapping(path = "/password-reset",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
    	OperationStatusModel returnValue = new OperationStatusModel();
 
        boolean operationResult = userService.resetPassword(
                passwordResetModel.getToken(),
                passwordResetModel.getPassword());
        
        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
 
        if(operationResult)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }

}
