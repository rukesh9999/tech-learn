/**
 * 
 */
package com.tech.rukesh.techlearn.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.rukesh.techlearn.dto.InboxMailsResponse;
import com.tech.rukesh.techlearn.service.InboxMailsService;

/**
 * @author Rukesh
 *
 */

@RequestMapping("/InboxMails")
@RestController
public class InboxMailsController {

	final static Logger logger = LoggerFactory.getLogger(InboxMailsController.class);

	
	@Autowired
	private InboxMailsService inboxMailsService;
	
	@GetMapping("/all")
	public ResponseEntity<List<InboxMailsResponse>>  getAllInboxMails() 
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		List<InboxMailsResponse>  listOfmails =    inboxMailsService.getAllInboxMails();
		
	    logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return new ResponseEntity<List<InboxMailsResponse>>(listOfmails,HttpStatus.OK);
	}
	
	 
}
