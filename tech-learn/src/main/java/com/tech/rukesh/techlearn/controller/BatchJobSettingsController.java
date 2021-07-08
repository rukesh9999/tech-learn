/**
 * 
 */
package com.tech.rukesh.techlearn.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.rukesh.techlearn.dto.BatchJobSettingsRequest;
import com.tech.rukesh.techlearn.dto.BatchJobSettingsResponse;
import com.tech.rukesh.techlearn.dto.BatchJobSettingsUpdateRequest;
import com.tech.rukesh.techlearn.service.BatchJobSettingsService;


/**
 * @author Rukesh
 *
 */

@RequestMapping("/BatchJobSettings")
@RestController
public class BatchJobSettingsController {
	
	@Autowired
	private BatchJobSettingsService batchJobSettingsService;
	
	final static Logger logger = LoggerFactory.getLogger(BatchJobSettingsService.class);

	
	@PostMapping("/save")
	public ResponseEntity<String> saveUserBatchJobSettings(@Valid @RequestBody BatchJobSettingsRequest batchJobSettingsRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

	   	String status  =   batchJobSettingsService.saveUserBatchJobSettings(batchJobSettingsRequest);
		
	   	logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

	   	return new ResponseEntity<String>(status,HttpStatus.CREATED);
		
	}
	
	
	@GetMapping("/all")
	public ResponseEntity<List<BatchJobSettingsResponse>> getAllBatchJobSettings()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		List<BatchJobSettingsResponse> listOfBatchJobSettings  =  batchJobSettingsService.getAllBatchJobSettings();
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return new ResponseEntity<List<BatchJobSettingsResponse>>(listOfBatchJobSettings,HttpStatus.OK);
			
	}
	
	
	@GetMapping("/get/{id}")
	public ResponseEntity<BatchJobSettingsResponse> getBatchJobSettingsById(@PathVariable("id") Integer BatchJobSettingsId)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		BatchJobSettingsResponse batchJobSettingsResponse =   batchJobSettingsService.getBatchJobSettingsById(BatchJobSettingsId);
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return new ResponseEntity<BatchJobSettingsResponse>(batchJobSettingsResponse,HttpStatus.OK);
	}
	
	
	@PutMapping("/update")
	public  ResponseEntity<String> updateBatchJobSettings(@Valid @RequestBody BatchJobSettingsUpdateRequest batchJobSettingsUpdateRequest)
	{
		  logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

	      String status =  batchJobSettingsService.updateBatchJobSettings(batchJobSettingsUpdateRequest);
	          
		  logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
        
		  return new ResponseEntity<String>(status,HttpStatus.OK);
		  
	}
	
   
}
