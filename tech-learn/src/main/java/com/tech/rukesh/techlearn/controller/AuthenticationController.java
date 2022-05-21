/**
 * 
 */
package com.tech.rukesh.techlearn.controller;

import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tech.rukesh.techlearn.dto.AuthenticationResponse;
import com.tech.rukesh.techlearn.dto.LoginRequest;
import com.tech.rukesh.techlearn.dto.RefreshTokenRequest;
import com.tech.rukesh.techlearn.dto.UserRegistrationRequest;
import com.tech.rukesh.techlearn.model.PasswordResetToken;
import com.tech.rukesh.techlearn.service.AuthenticationService;
import com.tech.rukesh.techlearn.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

/**
 * @author Rukesh
 *
 */
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

	final static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		String status = authenticationService.registerUser(userRegistrationRequest);
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return new ResponseEntity<String>(status,HttpStatus.CREATED);
	}
	
	@GetMapping("/verifyUserExistsORNot/{username}")
	public ResponseEntity<String> verifyUserExistsORNot(@PathVariable("username")String username)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		String status = authenticationService.verifyUserExistsORNot(username);
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
	  
	  
	  @GetMapping("/forgotpassword/{email}")
	  public ResponseEntity<String> forgotPassword(@PathVariable("email") String email)
	  {
		   logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		   authenticationService.forgotPassword(email);
		  
		   logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		   return null;
		  
	  }
	  
	  @GetMapping("/changepassword/{updatedpassword}/{token}")
	  public ResponseEntity<String> changePassword(@PathVariable("updatedpassword")String updatedpassword,@PathVariable("token")String token)
	  {
		   logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		   
           String  status =  authenticationService.changepassword(updatedpassword,token);
		   
		   logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		   
		   return new ResponseEntity<String>(status,HttpStatus.OK);

	  }
	 
	  
	  @PostMapping("/logout")
	  public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
	  {
		  refreshTokenService.deleteToken(refreshTokenRequest.getRefreshToken());
		  return ResponseEntity.status(HttpStatus.OK).body("Refresh Token deleted successfully");
	  }
}
