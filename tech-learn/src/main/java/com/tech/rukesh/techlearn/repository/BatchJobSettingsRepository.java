/**
 * 
 */
package com.tech.rukesh.techlearn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.tech.rukesh.techlearn.model.BatchJobSettings;

/**
 * @author Rukesh
 *
 */
@Repository
public interface BatchJobSettingsRepository extends PagingAndSortingRepository<BatchJobSettings, Integer>{

	/**
	 * @param userName
	 * @return
	 */
	Optional<BatchJobSettings> findByUserName(String userName);

}
