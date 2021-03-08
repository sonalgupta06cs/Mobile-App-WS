package com.springboottest.app.ws.entity.repositry;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboottest.app.ws.io.entity.UserEntity;

/**
 * 
 * @author SonaSach
 * 
 * Spring Data JPA framework was made to make our work easy to work with data base.
 *  
 * With Spring Data JPA framework, it becomes really easier to perform the db crud operations.
 * We have to just pass the entity that needs to be persisted & the data type of the id.
 * 
 * So, Here, we don't need to provide any methods in order to be able to save, update, delete, create
 * but if you want to implement the custom methods implementations 
 * like findUserBy, deleteUserbyPublicUserId, addUserByEmailAddress, findUserByEmailAddress 
 * 
 * Implementation of methods is done by spring at run time.
 * 
 * PagingAndSortingRepository is provided by Spring for pagination, like function findAll(Pageable)
 */
@Repository
public interface UserRepository extends  PagingAndSortingRepository<UserEntity, Long> {
	
	//UserEntity findUserByEmailAdd(String email);
	
	UserEntity findByEmail(String email);
	UserEntity findUserByUserId(String id);
	
	UserEntity findUserByEmailVerificationToken(String token); 
	
	@Query(value="select * from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'", 
			countQuery="select count(*) from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'", 
			nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress( Pageable pageableRequest );
	
	@Query(value="select * from Users u where u.first_name = ?1",nativeQuery=true)
	List<UserEntity> findUserByFirstName(String firstName);
	
	@Query(value="select * from Users u where u.last_name = :lastName",nativeQuery=true)
	List<UserEntity> findUserByLastName(@Param("lastName") String lastName);
	
	@Query(value="select * from Users u where first_name LIKE %:keyword% or last_name LIKE %:keyword%",nativeQuery=true)
	List<UserEntity> findUsersByKeyword(@Param("keyword") String keyword);
	
	@Query(value="select u.first_name, u.last_name from Users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%",nativeQuery=true)
	List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);
	
	@Transactional
	@Modifying
	@Query(value="update users u set u.EMAIL_VERIFICATION_STATUS=:emailVerificationStatus where u.user_id=:userId", nativeQuery=true)
	void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, 
			@Param("userId") String userId);
	
//	@Query("select user from UserEntity user where user.userId =:userId")
//	UserEntity findUserEntityByUserId(@Param("userId") String userId);
	
//	@Query("select user.firstName, user.lastName from UserEntity user where user.userId =:userId")
//	List<Object[]> getUserEntityFullNameById(@Param("userId") String userId);
	
//    @Modifying
//    @Transactional 
//    @Query("UPDATE UserEntity u set u.emailVerificationStatus =:emailVerificationStatus where u.userId = :userId")
//    void updateUserEntityEmailVerificationStatus(
//    		@Param("emailVerificationStatus") boolean emailVerificationStatus,
//            @Param("userId") String userId);

}
