/**
 * 
 */
package com.appsdevelopeblog.app.ws.service;

import java.util.List;

import com.appsdevelopeblog.app.ws.shared.dto.AddressDTO;

/**
 * @author SonaSach
 *
 */
public interface AddressService {

	List<AddressDTO> getUserAddressesByUserId(String userId);

	AddressDTO getUserAddressByAddId(String userId);

}
