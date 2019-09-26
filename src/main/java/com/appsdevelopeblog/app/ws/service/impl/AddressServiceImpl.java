/**
 * 
 */
package com.appsdevelopeblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdevelopeblog.app.ws.entity.repositry.AddressRepository;
import com.appsdevelopeblog.app.ws.entity.repositry.UserRepository;
import com.appsdevelopeblog.app.ws.io.entity.AddressEntity;
import com.appsdevelopeblog.app.ws.io.entity.UserEntity;
import com.appsdevelopeblog.app.ws.service.AddressService;
import com.appsdevelopeblog.app.ws.shared.dto.AddressDTO;

/**
 * @author SonaSach
 *
 */
@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public List<AddressDTO> getUserAddressesByUserId(String userId) {
		
		List<AddressDTO> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = userRepository.findUserByUserId(userId);
		if(userEntity == null) return returnValue;
		
		// Do this below or create AddressRepository
		 List<AddressEntity> addresses = userEntity.getAddresses();
		
		// Fetching users addresses from address table
		//Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		 for(AddressEntity addressEntity:addresses)
	     {
	         returnValue.add( modelMapper.map(addressEntity, AddressDTO.class) );
	     }
		
		return returnValue;
	}

	@Override
	public AddressDTO getUserAddressByAddId(String addressId) {
		
		AddressDTO returnValue = null;
		                                                
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		if(addressEntity != null) {
			
			returnValue = new ModelMapper().map(addressEntity, AddressDTO.class);
			
		}
		return returnValue;
	}

}
