/**
 * 
 */
package com.tech.rukesh.techlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tech.rukesh.techlearn.model.MailAcknowledgement;

/**
 * @author Rukesh
 *
 */
@Repository
public interface MailAcknowledgementRepository  extends JpaRepository<MailAcknowledgement, Integer>{

	

}
