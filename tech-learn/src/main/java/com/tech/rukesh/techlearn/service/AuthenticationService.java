/**
 * 
 */
package com.tech.rukesh.techlearn.service;

import java.time.Instant;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.rukesh.techlearn.controller.TechnoloyController;
import com.tech.rukesh.techlearn.dto.AuthenticationResponse;
import com.tech.rukesh.techlearn.dto.LoginRequest;
import com.tech.rukesh.techlearn.dto.MailAcknowledgementDto;
import com.tech.rukesh.techlearn.dto.RefreshTokenRequest;
import com.tech.rukesh.techlearn.dto.RegistrationRequest;
import com.tech.rukesh.techlearn.exception.NoSuchUserExistsException;
import com.tech.rukesh.techlearn.exception.UserAlreadyExistsException;
import com.tech.rukesh.techlearn.exception.UserRegistrationException;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.RegistrationRepository;
import com.tech.rukesh.techlearn.security.JWTProvider;
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

	@Value("${mail.fromAddress}")
	private String fromAddress;
	
	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private PasswordGenerator passwordGenerator;
	
	@Autowired
	private MailManagerService mailManagerService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTProvider jwtProvider;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	
	  @Autowired 
	  private PasswordEncoder passwordEncoder;
	 
	
	
	final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	
	
	public String registerUser(RegistrationRequest registrationRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		String generatedPassword = passwordGenerator.generatePassword();
		Optional<UserRegistration> optUser = registrationRepository.findByEmail(registrationRequest.getEmail());
		if(optUser.isPresent())
		throw new UserAlreadyExistsException("UserAlreadyExists");
		UserRegistration userRegistration = mapFromUserRegistrationRequest(registrationRequest);
		userRegistration.setPassword(passwordEncoder.encode(generatedPassword));
		try {
		    registrationRepository.save(userRegistration);
		}catch (Exception e) {
			throw new UserRegistrationException(e.getMessage());
		}
		
		
		MailAcknowledgementDto mailAcknowledgementDto = new MailAcknowledgementDto();
		mailAcknowledgementDto.setUserId(userRegistration.getUserId());
		mailAcknowledgementDto.setToAddress(userRegistration.getEmail());
		mailAcknowledgementDto.setTypeOfMail("AccountCreation");
		mailAcknowledgementDto.setSubject("New Account Created");
		mailAcknowledgementDto.setFromAddress(fromAddress);
		mailManagerService.sendAccountCreatedAcknowledgeMail(mailAcknowledgementDto,generatedPassword);
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return "User Registered successfully";
	}
	
	
	
	public AuthenticationResponse login(LoginRequest loginRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

	   Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
	
	   SecurityContextHolder.getContext().setAuthentication(authentication);
	   String athenticationToken = jwtProvider.generateToken(authentication);
	    
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

	   return AuthenticationResponse.builder()
			   .athenticationToken(athenticationToken)
			   .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
			   .userName(loginRequest.getUserName())
			   .refreshToken(refreshTokenService.generateRefreshToken().getToken())
			   .build();
			   
			  
	   
	}
	
	
	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest)
	{
		 logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		 refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		 String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUserName());
		 
		 logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		 return AuthenticationResponse.builder()
				  .athenticationToken(token)
				  .refreshToken(refreshTokenRequest.getRefreshToken())
				  .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				  .userName(refreshTokenRequest.getUserName())
				  .build();
			
				 
	}
	 
	  @Transactional(readOnly = true)
	  public UserRegistration getCurrentUser()
	  {
		 User principle =   (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 
		 return registrationRepository.findByEmail(principle.getUsername()).orElseThrow(()->new NoSuchUserExistsException("user doesnot exists "+principle.getUsername()));
	  }
	
	
	public UserRegistration mapFromUserRegistrationRequest(RegistrationRequest registrationRequest)
	{
		return UserRegistration.builder()
				.firstName(registrationRequest.getFirstName())
				.lastName(registrationRequest.getLastName())
				.email(registrationRequest.getEmail())
				.build();
	}
	
	
	
}
