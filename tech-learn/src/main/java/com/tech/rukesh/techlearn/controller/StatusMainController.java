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

import com.tech.rukesh.techlearn.dto.StatusMainRequest;
import com.tech.rukesh.techlearn.dto.StatusMainResponse;
import com.tech.rukesh.techlearn.dto.StatusMainUpdateRequest;
import com.tech.rukesh.techlearn.service.StatusMainService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/statusmain")
@RestController
public class StatusMainController {
	
	@Autowired
	private StatusMainService statusMainService;
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);

	
	@PostMapping("/save")
	public ResponseEntity<String> saveStatusMain(@Valid @RequestBody StatusMainRequest statusMainDto){
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		String status = statusMainService.saveStatusMain(statusMainDto);
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<String>(status,HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<StatusMainResponse>> getAllStatusMains()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
	    List<StatusMainResponse> listofstatusmains = statusMainService.getAllStatusMain();		
	    logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<List<StatusMainResponse>>(listofstatusmains,HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<StatusMainResponse> getStatusMainById(@PathVariable("id") Integer id)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		StatusMainResponse statusMainResponse =  statusMainService.getStatusMainById(id);
	    logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<StatusMainResponse>(statusMainResponse,HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateStatusMainDto(@Valid @RequestBody StatusMainUpdateRequest statusMainUpdateRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		String status =  statusMainService.updateStatusMain(statusMainUpdateRequest);
	    logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<String>(status,HttpStatus.OK);
	}
}
