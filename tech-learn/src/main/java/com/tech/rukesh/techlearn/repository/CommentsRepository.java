/**
 * 
 */
package com.tech.rukesh.techlearn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tech.rukesh.techlearn.model.Comments;

/**
 * @author Rukesh
 *
 */
public interface CommentsRepository extends JpaRepository<Comments, Integer>{

	/**
	 * @param id
	 */
	List<Comments> findByTechnoloyId(Integer id);

}
