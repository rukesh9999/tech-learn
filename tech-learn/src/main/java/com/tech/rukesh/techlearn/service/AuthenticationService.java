/**
 * 
 */
package com.tech.rukesh.techlearn.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

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
import com.tech.rukesh.techlearn.dto.UserRegistrationRequest;
import com.tech.rukesh.techlearn.exception.NoSuchUserExistsException;
import com.tech.rukesh.techlearn.exception.TechLearnException;
import com.tech.rukesh.techlearn.exception.UserAlreadyExistsException;
import com.tech.rukesh.techlearn.exception.UserRegistrationException;
import com.tech.rukesh.techlearn.model.PasswordResetToken;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.MailAcknowledgementRepository;
import com.tech.rukesh.techlearn.repository.PasswordResetTokenRepository;
import com.tech.rukesh.techlearn.repository.UserRegistrationRepository;
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
	private UserRegistrationRepository userRegistrationRepository;
	
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
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	
	  @Autowired 
	  private PasswordEncoder passwordEncoder;
	 
	
	
	final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	
	
	public String registerUser(UserRegistrationRequest userRegistrationRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		StringBuilder message = new StringBuilder();
		String generatedPassword = passwordGenerator.generatePassword();
		Optional<UserRegistration> optUser = userRegistrationRepository.findByEmail(userRegistrationRequest.getEmail());
		if(optUser.isPresent())
		throw new UserAlreadyExistsException("UserAlreadyExists");
		UserRegistration userRegistration = mapFromUserRegistrationRequest(userRegistrationRequest);
		userRegistration.setPassword(passwordEncoder.encode(generatedPassword));
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
		Boolean sendAccountCreatedAcknowledgeMail = mailManagerService.sendAccountCreatedAcknowledgeMail(mailAcknowledgementDto,generatedPassword);
		
		if(sendAccountCreatedAcknowledgeMail)
		message.append("User Registered successfully");
		
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return message.toString();
	}
	
	
	
	public AuthenticationResponse login(LoginRequest loginRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
       String fullName="";
       Integer userId =null;
	   Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
	
	   SecurityContextHolder.getContext().setAuthentication(authentication);
	   String authenticationToken = jwtProvider.generateToken(authentication);
	   Optional<UserRegistration> optUser = userRegistrationRepository.findByEmail(loginRequest.getUserName());
       if(optUser.isPresent())
       {
    	   fullName =   optUser.get().getFirstName()+"   "+optUser.get().getLastName();
    	   userId = optUser.get().getUserId();      
    	}
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

	   return AuthenticationResponse.builder()
			   .authenticationToken(authenticationToken)
			   .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
			   .userName(loginRequest.getUserName())
			   .refreshToken(refreshTokenService.generateRefreshToken().getToken())
			   .fullName(fullName)
			   .userId(userId)
			   .build();
			   
			  
	   
	}
	
	
	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest)
	{
		 logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		 refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		 String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUserName());
		 
		 logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		 return AuthenticationResponse.builder()
				  .authenticationToken(token)
				  .refreshToken(refreshTokenRequest.getRefreshToken())
				  .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				  .userName(refreshTokenRequest.getUserName())
				  .build();	 
	}
	 
	  @Transactional(readOnly = true)
	  public UserRegistration getCurrentUser()
	  {
		 User principle =   (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 
		 return userRegistrationRepository.findByEmail(principle.getUsername()).orElseThrow(()->new NoSuchUserExistsException("user doesnot exists "+principle.getUsername()));
	  }
	
	
	public UserRegistration mapFromUserRegistrationRequest(UserRegistrationRequest userRegistrationRequest)
	{
		return UserRegistration.builder()
				.firstName(userRegistrationRequest.getFirstName())
				.lastName(userRegistrationRequest.getLastName())
				.email(userRegistrationRequest.getEmail())
				.build();
	}



	public String verifyUserExistsORNot(String username) {
		 String existsOrNot="";
		 Optional<UserRegistration> optUserRegistration = userRegistrationRepository.findByEmail(username);
	     if(optUserRegistration.isPresent())
	     {
	    	 existsOrNot+="exists";
	     }else {
	    	 existsOrNot+="not exists";
	     }
		return existsOrNot;
	}



	public String  forgotPassword(String email) {
		 logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
        StringBuilder message =new StringBuilder();
		Optional<UserRegistration> optEmail = userRegistrationRepository.findByEmail(email);
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		String token =UUID.randomUUID().toString();
		passwordResetToken.setToken(token);
		passwordResetToken.setUserRegistration(optEmail.get());
		LocalDateTime now = LocalDateTime.now();
		passwordResetToken.setExpiryDate(now);
		
		try {
		    passwordResetTokenRepository.save(passwordResetToken);
		}catch (Exception e) {
			throw new TechLearnException(e.getMessage());
		}
		
		
		MailAcknowledgementDto mailAcknowledgementDto = new MailAcknowledgementDto();
		mailAcknowledgementDto.setUserId(optEmail.get().getUserId());
		mailAcknowledgementDto.setToAddress(optEmail.get().getEmail());
		mailAcknowledgementDto.setTypeOfMail("ForgotPassword");
		mailAcknowledgementDto.setSubject("Forgot Password");
		mailAcknowledgementDto.setFromAddress(fromAddress);

		Boolean sendForgotPasswordMail = mailManagerService.sendForgotPasswordMail(mailAcknowledgementDto,token);
		if(sendForgotPasswordMail)
		{
			message.append("Forgot password generated successfully");
		}else {
			message.append("Fail to generate forgot password");
		}
		
		 logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return message.toString();
	}



	public String changepassword(String updatedpassword,String token) {
		
		   logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		   PasswordResetToken passwordResetToken=null;
		   LocalDateTime currentDate=null;
		   LocalDateTime expirationDate =null;
		   Boolean flag=false;
		   String message ="";
		   Optional<PasswordResetToken> optToken = passwordResetTokenRepository.findByToken(token);
		     
		     if(optToken.isPresent()) {
		        passwordResetToken = optToken.get();
		        expirationDate = passwordResetToken.getExpiryDate();
		        currentDate = LocalDateTime.now();
		        long hours = ChronoUnit.HOURS.between(currentDate, expirationDate);
		        logger.info("hours...."+hours);
		        
		        if(hours>=24) {
		          throw new TechLearnException("token expired");
		        }else {
		        	UserRegistration userRegistration = passwordResetToken.getUserRegistration();
		        	userRegistration.setPassword(passwordEncoder.encode(updatedpassword));
		        	userRegistrationRepository.save(userRegistration);
		        	
		        	flag=true;
		        }
		        
		     }
		     
		     if(flag==true)
		     {
		    	 passwordResetTokenRepository.delete(passwordResetToken);
		    	 message+="pssword updated successfully";
		     }
		     
			 logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		     return message;
	}
	
	
	
}
