package com.tech.rukesh.techlearn.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.tech.rukesh.techlearn.model.StatusMain;
@Repository
public interface StatusMainRepository extends  PagingAndSortingRepository<StatusMain, Integer> {

	/**
	 * @param name
	 */
	Optional<StatusMain> findByName(String name);

}
