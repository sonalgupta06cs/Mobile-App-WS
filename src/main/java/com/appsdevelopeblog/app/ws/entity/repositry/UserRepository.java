package com.appsdevelopeblog.app.ws.entity.repositry;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.appsdevelopeblog.app.ws.io.entity.UserEntity;

/**
 * 
 * @author SonaSach
 * 
 * Spring Data JPA framework was made to make our work easy to work with data base.
 *  
 * With Spring Data JPA framework, it becomes really easier to perform the db crud operations.
 * We have to just pass the entity that needs to be persisted & the data type of the id.
 * 
 * So, Here, we dont need to provide any methods in order to be able to save, update, delete, create
 * but if you want to implement the custom methods implementations 
 * like findUserBy, deleteUserbyPublicUserId, addUserByEmailAddress, findUserByEmailAddress 
 * 
 * PagingAndSortingRepository is provided by Spring for pagination, like function findAll(Pageable)
 */
@Repository
public interface UserRepository extends  PagingAndSortingRepository<UserEntity, Long> {//CrudRepository<UserEntity, Long> {
	
	//UserEntity findUserByEmailAdd(String email);
	
	UserEntity findByEmail(String email);
	UserEntity findUserByUserId(String id);

}
