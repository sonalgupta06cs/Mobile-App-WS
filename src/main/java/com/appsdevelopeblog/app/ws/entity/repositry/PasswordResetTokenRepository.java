package com.appsdevelopeblog.app.ws.entity.repositry;

import org.springframework.data.repository.CrudRepository;

import com.appsdevelopeblog.app.ws.io.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long>{
	PasswordResetTokenEntity findByToken(String token);
}
