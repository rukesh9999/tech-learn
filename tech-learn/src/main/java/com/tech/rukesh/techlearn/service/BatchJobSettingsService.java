/**
 * 
 */
package com.tech.rukesh.techlearn.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.rukesh.techlearn.dto.BatchJobSettingsRequest;
import com.tech.rukesh.techlearn.dto.BatchJobSettingsResponse;
import com.tech.rukesh.techlearn.dto.BatchJobSettingsUpdateRequest;
import com.tech.rukesh.techlearn.exception.BatchJobSettingsAlreadyExistsException;
import com.tech.rukesh.techlearn.exception.BatchJobSettingsException;
import com.tech.rukesh.techlearn.exception.NoSuchBatchJobSettingsException;
import com.tech.rukesh.techlearn.model.BatchJobSettings;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.BatchJobSettingsRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author Rukesh
 *
 */
@RequiredArgsConstructor
@Transactional
@Service
public class BatchJobSettingsService {

	@Autowired
	private BatchJobSettingsRepository batchJobSettingsRepository;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	final static Logger logger = LoggerFactory.getLogger(BatchJobSettingsService.class);

	public String saveUserBatchJobSettings(BatchJobSettingsRequest batchJobSettingsRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		 BatchJobSettings batchJobSettings=null;
		 Optional<BatchJobSettings> batchJobSettingsopt =  batchJobSettingsRepository.findByUserName(batchJobSettingsRequest.getUserName());
		 
		 if(batchJobSettingsopt.isPresent())
		 throw new BatchJobSettingsAlreadyExistsException("BatchJobSettingsAlreadyExists with this EmailId ");
		 else
		 batchJobSettings = mapFromBatchJobSettingsRequest(batchJobSettingsRequest);
		 try {
		        batchJobSettingsRepository.save(batchJobSettings);
		     }catch (Exception e) { 
			    throw new BatchJobSettingsException(e.getMessage());
		     }
		
		
		 logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		 
		 return "BatchJobSettings saved successfully";
	}
	
	
	public List<BatchJobSettingsResponse> getAllBatchJobSettings()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		List<BatchJobSettings>  batchJobSettingsList =  (List<BatchJobSettings>) batchJobSettingsRepository.findAll();
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return batchJobSettingsList.stream()
				                   .map(this::mapToBatchJobSettingsResponse)
				                   .collect(Collectors.toList());
	}
	
	
	
	public BatchJobSettingsResponse getBatchJobSettingsById(Integer BatchJobSettingsId)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		Optional<BatchJobSettings>   BatchJobSettingsOpt =  batchJobSettingsRepository.findById(BatchJobSettingsId);
		if(!BatchJobSettingsOpt.isPresent())
		throw new NoSuchBatchJobSettingsException("No Such BatchJobSettings Exists");
		else
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return mapToBatchJobSettingsResponse(BatchJobSettingsOpt.get());
		
	}
	
	
	
	public String updateBatchJobSettings(BatchJobSettingsUpdateRequest batchJobSettingsUpdateRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		BatchJobSettingsResponse batchJobSettingsResponseDB =  getBatchJobSettingsById(batchJobSettingsUpdateRequest.getId());
		
	    UserRegistration userRegistration =    authenticationService.getCurrentUser();
		
		BatchJobSettings batchJobSettings = new BatchJobSettings();
		batchJobSettings.setId(batchJobSettingsUpdateRequest.getId());
		batchJobSettings.setHost(batchJobSettingsUpdateRequest.getHost());
		batchJobSettings.setPassword(batchJobSettingsUpdateRequest.getPassword());
		batchJobSettings.setPortNo(batchJobSettingsUpdateRequest.getPortNo());
		batchJobSettings.setProtocal(batchJobSettingsUpdateRequest.getProtocal());
		batchJobSettings.setUserName(batchJobSettingsUpdateRequest.getUserName());
		batchJobSettings.setUserRegistration(userRegistration);
	   
		try {
		   batchJobSettingsRepository.save(batchJobSettings);
		}catch (Exception e) {
		  throw new BatchJobSettingsException(e.getMessage());
		}
			
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return "BatchJobSettings Updated successfully";
	}
	
	
	


	/**
	 * @param batchJobSettingsRequest
	 * @return
	 */
	private BatchJobSettings mapFromBatchJobSettingsRequest(BatchJobSettingsRequest batchJobSettingsRequest) {
		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

	    UserRegistration userRegistration =  authenticationService.getCurrentUser();
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		
		return  BatchJobSettings.builder()
		.portNo(batchJobSettingsRequest.getPortNo())
		.protocal(batchJobSettingsRequest.getProtocal())
		.userName(batchJobSettingsRequest.getUserName())
		.password(batchJobSettingsRequest.getPassword())
		.host(batchJobSettingsRequest.getHost())
		.userRegistration(userRegistration)
		.enableAutoConvertToTechnology(batchJobSettingsRequest.getEnableAutoConvertToTechnology())
		.build();
	  
		
	}
	
	/**
	 * @author Rukesh
	 * @param batchJobSettings
	 * @return
	 */
	public BatchJobSettingsResponse  mapToBatchJobSettingsResponse(BatchJobSettings batchJobSettings)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		String fullName = batchJobSettings.getUserRegistration().getFirstName()+"   "+batchJobSettings.getUserRegistration().getLastName();
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return BatchJobSettingsResponse.builder()
				 .host(batchJobSettings.getHost())
				 .portNo(batchJobSettings.getPortNo())
				 .protocal(batchJobSettings.getProtocal())
				 .userName(batchJobSettings.getUserName())
				 .password(batchJobSettings.getPassword())
				 .submitter(fullName)
				 .build();	
	}
	
	
}
