/**
 * 
 */
package com.springboottest.app.ws.service;

import java.util.List;

import com.springboottest.app.ws.shared.dto.AddressDTO;

/**
 * @author SonaSach
 *
 */
public interface AddressService {

	List<AddressDTO> getUserAddressesByUserId(String userId);

	AddressDTO getUserAddressByAddId(String userId);

}
