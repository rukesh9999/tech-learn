/**
 * 
 */
package com.tech.rukesh.techlearn.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.tech.rukesh.techlearn.model.InboxMails;

/**
 * @author Rukesh
 *
 */
public interface InboxMailsRepository extends PagingAndSortingRepository<InboxMails, Integer>{

}
