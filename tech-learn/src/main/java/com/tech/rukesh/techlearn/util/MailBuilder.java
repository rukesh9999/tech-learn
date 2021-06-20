/**
 * 
 */
package com.tech.rukesh.techlearn.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.JmsProperties.AcknowledgeMode;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.tech.rukesh.techlearn.controller.TechnoloyController;
import com.tech.rukesh.techlearn.dto.MailAcknowledgementDto;
import com.tech.rukesh.techlearn.exception.NoSuchUserExistsException;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.UserRegistrationRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author Rukesh
 *
 */
@Component
@RequiredArgsConstructor
public class MailBuilder {

	@Value("${app.url}")
	private String url;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private UserRegistrationRepository userRegistrationRepository;
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);

	
	public String build(Integer userid)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		Optional<UserRegistration> userRegistration =  userRegistrationRepository.findById(userid);
		
		Context context=null;
		
		if(!userRegistration.isPresent())
	    throw new NoSuchUserExistsException("NoSuchUserExists");
		else	
		context =new Context();
		UserRegistration userregistration = userRegistration.get();
		context.setVariable("firstName", userregistration.getFirstName());
		context.setVariable("lastName", userregistration.getLastName());
		context.setVariable("email", userregistration.getEmail());
		context.setVariable("password", userregistration.getPassword());
		logger.info("url ..."+url);

		context.setVariable("url", url);
		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return templateEngine.process("NewAccountCreatedmailTemplate", context);
	}
	

}
