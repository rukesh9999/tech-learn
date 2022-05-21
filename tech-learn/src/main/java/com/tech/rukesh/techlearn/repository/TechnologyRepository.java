package com.tech.rukesh.techlearn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.tech.rukesh.techlearn.model.Technoloy;
@Repository
public interface TechnologyRepository extends PagingAndSortingRepository<Technoloy, Integer>{

	Optional<Technoloy> findByName(String name);
    
	@Query(value = "select max(code) from technology t ",nativeQuery = true)
	String getTechnologyCodeById();
	
	
	Integer countBystatusMainIdAndUserRegistrationUserId(Integer statusId,Integer userId);
			
}
