/**
 * 
 */
package com.tech.rukesh.techlearn.service;

import java.util.Optional;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.rukesh.techlearn.controller.TechnoloyController;
import com.tech.rukesh.techlearn.dto.MailAcknowledgementDto;
import com.tech.rukesh.techlearn.exception.NoSuchUserExistsException;
import com.tech.rukesh.techlearn.exception.UserAlreadyExistsException;
import com.tech.rukesh.techlearn.model.MailAcknowledgement;
import com.tech.rukesh.techlearn.model.Technoloy;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.MailAcknowledgementRepository;
import com.tech.rukesh.techlearn.repository.UserRegistrationRepository;
import com.tech.rukesh.techlearn.util.MailBuilder;

import lombok.RequiredArgsConstructor;

/**
 * @author Rukesh
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MailManagerService {

	@Autowired
	private MailBuilder mailBuilder;
	
	@Autowired
	private MailAcknowledgementRepository mailAcknowledgementRepository;
	
	@Autowired
	private UserRegistrationRepository userRegistrationRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);

	
	public Boolean sendAccountCreatedAcknowledgeMail(MailAcknowledgementDto mailAcknowledgementDto)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		Boolean status=false;	
		String  html = mailBuilder.buildRegistration(mailAcknowledgementDto.getUserId());
		mailAcknowledgementDto.setBody(html);
		this.saveMailDetailsBeforeSend(mailAcknowledgementDto);
		this.sendMail(mailAcknowledgementDto);	
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
        return status;
	}
	
	
	
	public Boolean sendTechnologyAcknowledgeRelatedMail(MailAcknowledgementDto mailAcknowledgementDto,Technoloy technoloy)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		String html=mailBuilder.buildStatusUpdate(technoloy,mailAcknowledgementDto.getTypeOfMail(),mailAcknowledgementDto.getUserId());
		
		mailAcknowledgementDto.setBody(html);
		this.saveMailDetailsBeforeSend(mailAcknowledgementDto);
		Boolean status = this.sendMail(mailAcknowledgementDto);
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
        return status;
	}
	
	
	 @Async
	 public Boolean  sendMail(MailAcknowledgementDto mailAcknowledgementDto)
	 {
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		Boolean status=false;

		MimeMessagePreparator mimeMessagePreparator = mimeMessage-> {
			
			 MimeMessageHelper mimemessagehelper = new MimeMessageHelper(mimeMessage);
			 mimemessagehelper.setFrom(mailAcknowledgementDto.getFromAddress());
			 mimemessagehelper.setTo(mailAcknowledgementDto.getToAddress());
			 mimemessagehelper.setSubject(mailAcknowledgementDto.getSubject());
			 mimemessagehelper.setText(mailAcknowledgementDto.getBody(),true);
						
		};
		
		try {
			logger.info("Before mail sent");
		    javaMailSender.send(mimeMessagePreparator);
			logger.info("After mail sent");
			status=true;
		}catch (Exception e) {
			e.printStackTrace();
			status=false;
		}
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
       return status;
	}
	 
	 
	
	public void saveMailDetailsBeforeSend(MailAcknowledgementDto mailAcknowledgementDto)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		MailAcknowledgement mailAcknowledgement = mapFromDto(mailAcknowledgementDto);
		logger.info("mailAcknowledgementDto..."+mailAcknowledgementDto);

		mailAcknowledgementRepository.save(mailAcknowledgement);
		
		logger.info("end into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

	}
	
	
	public MailAcknowledgement mapFromDto(MailAcknowledgementDto mailAcknowledgementDto)
	{
		Optional<UserRegistration> optUser = userRegistrationRepository.findById(mailAcknowledgementDto.getUserId());
		
		return MailAcknowledgement.builder()
				.fromAddress(mailAcknowledgementDto.getFromAddress())
				.toAddress(mailAcknowledgementDto.getToAddress())
				.subject(mailAcknowledgementDto.getSubject())
				.body(mailAcknowledgementDto.getBody())
				.typeOfMail(mailAcknowledgementDto.getTypeOfMail())
				.userRegistration(optUser.get())
				.build();
	}
	
	
}
