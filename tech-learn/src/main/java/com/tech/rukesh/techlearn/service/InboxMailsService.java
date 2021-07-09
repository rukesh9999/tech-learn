/**
 * 
 */
package com.tech.rukesh.techlearn.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.rukesh.techlearn.dto.InboxMailsDto;
import com.tech.rukesh.techlearn.dto.InboxMailsResponse;
import com.tech.rukesh.techlearn.exception.NoSuchUserExistsException;
import com.tech.rukesh.techlearn.exception.TechLearnException;
import com.tech.rukesh.techlearn.exception.UserAlreadyExistsException;
import com.tech.rukesh.techlearn.model.InboxMails;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.InboxMailsRepository;
import com.tech.rukesh.techlearn.repository.UserRegistrationRepository;
import com.tech.rukesh.techlearn.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * @author Rukesh
 *
 */

@RequiredArgsConstructor
@Transactional
@Service
public class InboxMailsService {
	
	@Autowired
	private InboxMailsRepository inboxMailsRepository;
	
	@Autowired
	private UserRegistrationRepository userRegistrationRepository;
	
	final static Logger logger = LoggerFactory.getLogger(InboxMailsService.class);

	
	
	public List<InboxMailsResponse> getAllInboxMails() {
	  
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

	    List<InboxMails> listOfInboxmails	 =  (List<InboxMails>) inboxMailsRepository.findAll();
		
	    logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

	    return listOfInboxmails.stream().map(this::mapToInboxMailsResponse).collect(Collectors.toList());
	}
	
	
	
	public void saveInboxMailsFromBatchJob(InboxMailsDto inboxMailsDto)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		InboxMails inboxMails  =  mapFromInboxMailsDto(inboxMailsDto);
		
		try {
			    inboxMailsRepository.save(inboxMails);			
		    }catch (Exception e) { 
		    	
			  throw new TechLearnException(e.getMessage());
		    }
		
	    logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
	}
	
	
	
	
	/**
	 * @param inboxMailsDto
	 * @return 
	 */
	private InboxMails mapFromInboxMailsDto(InboxMailsDto inboxMailsDto) {
		  
		Optional<UserRegistration> userRegistrationOpt = userRegistrationRepository.findById(inboxMailsDto.getUserId());
		
		if(!userRegistrationOpt.isPresent())
		throw new NoSuchUserExistsException("User does not exists");
		  
		return InboxMails.builder()
				.convertedToTechnology(inboxMailsDto.isAutoConvertToTechnology())
				.subject(inboxMailsDto.getSubject())
				.description(inboxMailsDto.getDescription())
				.fromAddress(inboxMailsDto.getFromAddress())
				.toAdddress(inboxMailsDto.getToAdddress())
				.userRegistration(userRegistrationOpt.get())
				.build();
	}



	public InboxMailsResponse mapToInboxMailsResponse(InboxMails InboxMails)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		Optional<UserRegistration> userRegistrationOpt =  userRegistrationRepository.findById(InboxMails.getUserRegistration().getUserId());
		
		String fullName="";
		
		if(!userRegistrationOpt.isPresent())
		throw new NoSuchUserExistsException("No such user exists");
		else
		fullName = userRegistrationOpt.get().getFirstName()+"   "+userRegistrationOpt.get().getLastName();	
		
	    logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return InboxMailsResponse.builder()
				.subject(InboxMails.getSubject())
				.description(InboxMails.getDescription())
				.fromAddress(InboxMails.getFromAddress())
				.toAdddress(InboxMails.getToAdddress())
				.mailSentDate(InboxMails.getMailSentDate())
				.autoConvertToTechnology(InboxMails.isConvertedToTechnology())
				.mailSentBy(fullName)
				.build();
	}
	
	
	
	
}
