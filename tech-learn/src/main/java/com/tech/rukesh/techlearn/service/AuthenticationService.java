/**
 * 
 */
package com.tech.rukesh.techlearn.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tech.rukesh.techlearn.controller.TechnoloyController;
import com.tech.rukesh.techlearn.dto.MailAcknowledgementDto;
import com.tech.rukesh.techlearn.dto.UserRegistrationDto;
import com.tech.rukesh.techlearn.exception.UserAlreadyExistsException;
import com.tech.rukesh.techlearn.exception.UserRegistrationException;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.UserRegistrationRepository;
import com.tech.rukesh.techlearn.util.PasswordGenerator;

import lombok.RequiredArgsConstructor;

/**
 * @author Rukesh
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

	@Autowired
	private UserRegistrationRepository userRegistrationRepository;
	
	@Autowired
	private PasswordGenerator passwordGenerator;
	
	@Autowired
	private MailManagerService mailManagerService;
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);
	
	@Value("${mail.fromAddress}")
	private String fromAddress;
	
	public UserRegistrationDto registerUser(UserRegistrationDto userRegistrationDto)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		Optional<UserRegistration> optUser = userRegistrationRepository.findByEmail(userRegistrationDto.getEmail());
		if(optUser.isPresent())
		throw new UserAlreadyExistsException("UserAlreadyExists");
		UserRegistration userRegistration = mapFromUserRegistrationDto(userRegistrationDto);
		userRegistration.setPassword(passwordGenerator.generatePassword());
		try {
		    userRegistrationRepository.save(userRegistration);
		}catch (Exception e) {
			throw new UserRegistrationException(e.getMessage());
		}
		
		
		MailAcknowledgementDto mailAcknowledgementDto = new MailAcknowledgementDto();
		mailAcknowledgementDto.setUserId(userRegistration.getUserId());
		mailAcknowledgementDto.setToAddress(userRegistration.getEmail());
		mailAcknowledgementDto.setTypeOfMail("AccountCreation");
		mailAcknowledgementDto.setSubject("New Account Created");
		mailAcknowledgementDto.setFromAddress(fromAddress);
		mailManagerService.sendAccountCreatedAcknowledgeMail(mailAcknowledgementDto);
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return mapToDto(userRegistration);
	}
	
	
	
	
	
	
	public UserRegistration mapFromUserRegistrationDto(UserRegistrationDto userRegistrationDto)
	{
		return UserRegistration.builder()
				.firstName(userRegistrationDto.getFirstName())
				.lastName(userRegistrationDto.getLastName())
				.email(userRegistrationDto.getEmail())
				.build();
	}
	
	
	public UserRegistrationDto mapToDto(UserRegistration userRegistration)
	{
		return UserRegistrationDto.builder()
				.userId(userRegistration.getUserId())
				.firstName(userRegistration.getFirstName())
				.lastName(userRegistration.getLastName())
				.email(userRegistration.getEmail())
				.password(userRegistration.getPassword())
				.build();
	}
}
