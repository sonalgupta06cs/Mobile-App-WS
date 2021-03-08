package com.springboottest.app.ws.entity.repositry;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springboottest.app.ws.io.entity.AddressEntity;
import com.springboottest.app.ws.io.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

	AddressEntity findByAddressId(String addressId);
	
}
