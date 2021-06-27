/**
 * 
 */
package com.tech.rukesh.techlearn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tech.rukesh.techlearn.model.RefreshToken;

/**
 * @author Rukesh
 *
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

	
	Optional<RefreshToken> findByToken(String token);

	
	void deleteByToken(String token);
	

}
