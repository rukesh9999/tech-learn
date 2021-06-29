/**
 * 
 */
package com.tech.rukesh.techlearn.util;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.tech.rukesh.techlearn.controller.TechnoloyController;
import com.tech.rukesh.techlearn.model.Comments;
import com.tech.rukesh.techlearn.model.StatusMain;
import com.tech.rukesh.techlearn.model.Technoloy;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.CommentsRepository;
import com.tech.rukesh.techlearn.repository.StatusMainRepository;
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
	
	@Autowired
	private StatusMainRepository statusMainRepository;
	
	@Autowired
	private CommentsRepository commentsRepository;
	
	
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);

	
	public String buildRegistration(Integer userid,String generatedPassword)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		Optional<UserRegistration> userRegistration =  userRegistrationRepository.findById(userid);
					
		Context context =new Context();
		UserRegistration userregistration = userRegistration.get();
		context.setVariable("firstName", userregistration.getFirstName());
		context.setVariable("lastName", userregistration.getLastName());
		context.setVariable("email", userregistration.getEmail());
		context.setVariable("password", generatedPassword);
		logger.info("url ..."+url);

		context.setVariable("url", url);
		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return templateEngine.process("NewAccountCreatedMailTemplate", context);
	}
	

	public String buildStatusUpdate(Technoloy technoloy,String typeOfMail,Integer userId)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		
		String comments="";
		String fileName="";
		if(typeOfMail.equalsIgnoreCase("TechnologyCreation"))
		fileName= "NewTechnologyCreatedMailTemplate";
		else if(typeOfMail.equalsIgnoreCase("TechnologyUpdated"))
		fileName="TechnologyStatusUpdateMailTemplate";	
		else 
	    fileName="TechnologyClosedMailTemplate";
		
		Optional<UserRegistration> userRegistration =  userRegistrationRepository.findById(userId);
		Optional<StatusMain> optStatus =  statusMainRepository.findById(technoloy.getStatusMain().getId());
		List<Comments>  ListOfComments =  commentsRepository.findByTechnoloyId(technoloy.getId());
		if(ListOfComments.size()>0)
		comments =ListOfComments.get(0).getComment();
		 	
		Context context =new Context();
		UserRegistration userregistration = userRegistration.get();
		context.setVariable("firstName", userregistration.getFirstName());
		context.setVariable("lastName", userregistration.getLastName());
		context.setVariable("code", technoloy.getCode());
		context.setVariable("name", technoloy.getName());
		context.setVariable("description", technoloy.getDescription());
		context.setVariable("totaltimetocomplete", technoloy.getTotalTimeToComplete());
		context.setVariable("status",optStatus.get().getName() );
		context.setVariable("comments",comments);
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return templateEngine.process(fileName, context);
	}
	
	
}
