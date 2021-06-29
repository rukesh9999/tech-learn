/**
 * 
 */
package com.tech.rukesh.techlearn.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.rukesh.techlearn.dto.AuthenticationResponse;
import com.tech.rukesh.techlearn.dto.LoginRequest;
import com.tech.rukesh.techlearn.dto.RefreshTokenRequest;
import com.tech.rukesh.techlearn.dto.RegistrationRequest;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.service.AuthenticationService;
import com.tech.rukesh.techlearn.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

/**
 * @author Rukesh
 *
 */
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		String status = authenticationService.registerUser(registrationRequest);
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return new ResponseEntity<String>(status,HttpStatus.OK);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		AuthenticationResponse authenticationResponse =  authenticationService.login(loginRequest);
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return new ResponseEntity<AuthenticationResponse>(authenticationResponse,HttpStatus.OK);
		
	}
	
	  @PostMapping("/refresh/token")
	  public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
	  {
		   logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		   AuthenticationResponse authenticationResponse =   authenticationService.refreshToken(refreshTokenRequest);
		 
		   logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		  return new ResponseEntity<AuthenticationResponse>(authenticationResponse,HttpStatus.OK);
	  }
	  
	  
	  
	 
	  
	  @PostMapping("/logout")
	  public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
	  {
		  refreshTokenService.deleteToken(refreshTokenRequest.getRefreshToken());
		  return ResponseEntity.status(HttpStatus.OK).body("Refresh Token deleted successfully");
	  }
}
