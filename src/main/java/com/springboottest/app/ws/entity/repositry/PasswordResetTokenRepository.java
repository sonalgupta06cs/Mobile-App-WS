package com.springboottest.app.ws.entity.repositry;

import org.springframework.data.repository.CrudRepository;

import com.springboottest.app.ws.io.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long>{
	PasswordResetTokenEntity findByToken(String token);
}
