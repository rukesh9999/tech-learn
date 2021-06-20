/**
 * 
 */
package com.tech.rukesh.techlearn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tech.rukesh.techlearn.model.UserRegistration;

/**
 * @author Rukesh
 *
 */
@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Integer>{

	/**
	 * @param email
	 */
	Optional<UserRegistration> findByEmail(String email);

	
}
