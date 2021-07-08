/**
 * 
 */
package com.tech.rukesh.techlearn.service;

import java.util.Collection;
import java.util.Collections;

import javax.transaction.Transactional;

import org.apache.commons.collections.iterators.SingletonListIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ibm.icu.impl.USerializedSet;
import com.tech.rukesh.techlearn.controller.TechnoloyController;
import com.tech.rukesh.techlearn.exception.NoSuchUserExistsException;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.UserRegistrationRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author Rukesh
 *
 */

@RequiredArgsConstructor
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService  {
	
	@Autowired
	private UserRegistrationRepository userRegistrationRepository;
	
	final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		UserRegistration userRegistration   = userRegistrationRepository.findByEmail(username).orElseThrow(()-> new NoSuchUserExistsException("user doesnot exists"));
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return new User(userRegistration.getEmail(), userRegistration.getPassword(), getGrantedAuthorities("user"));
	}

	
	
	private Collection<? extends GrantedAuthority> getGrantedAuthorities(String role) {
		
		return Collections.singletonList((new SimpleGrantedAuthority(role)));
	}



	
	
	
	
	
	
}
