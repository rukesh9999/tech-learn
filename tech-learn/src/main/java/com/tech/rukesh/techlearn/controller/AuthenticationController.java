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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.rukesh.techlearn.dto.UserRegistrationDto;
import com.tech.rukesh.techlearn.service.AuthenticationService;

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
	
	@PostMapping("/register")
	public ResponseEntity<UserRegistrationDto> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		UserRegistrationDto UserRegisteredDto = authenticationService.registerUser(userRegistrationDto);
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return new ResponseEntity<UserRegistrationDto>(UserRegisteredDto,HttpStatus.OK);
	}
	
	
	
}
