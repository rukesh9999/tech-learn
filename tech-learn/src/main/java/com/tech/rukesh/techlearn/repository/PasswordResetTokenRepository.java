package com.tech.rukesh.techlearn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tech.rukesh.techlearn.model.PasswordResetToken;

public interface PasswordResetTokenRepository  extends JpaRepository<PasswordResetToken, Integer>{

	Optional<PasswordResetToken> findByToken(String token);

	void deleteByToken(String token);

}
