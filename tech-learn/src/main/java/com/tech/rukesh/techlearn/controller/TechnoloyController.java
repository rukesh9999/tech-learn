package com.tech.rukesh.techlearn.controller;




import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tech.rukesh.techlearn.dto.TechnologyCommentsRequest;
import com.tech.rukesh.techlearn.dto.TechnologyStatusResponse;
import com.tech.rukesh.techlearn.dto.TechnoloyRequest;
import com.tech.rukesh.techlearn.dto.TechnoloyResponse;
import com.tech.rukesh.techlearn.service.TechnoloyService;

import lombok.RequiredArgsConstructor;
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/technology")
@RestController
public class TechnoloyController {

	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);
	
	@Autowired
	private TechnoloyService technoloyService;
	
	
	
	@PostMapping("/save")
	public ResponseEntity<String> saveTechnology(@Valid @RequestBody TechnoloyRequest technoloyrequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		String status = technoloyService.saveTechnology(technoloyrequest);	
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<String>(status,HttpStatus.OK);		
	}
	
	
	@GetMapping("/all")
	public ResponseEntity<List<TechnoloyResponse>> getAllTechnologies()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		List<TechnoloyResponse>  techlist = technoloyService.getAllTechnologies();
		logger.info("End of..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<List<TechnoloyResponse>>(techlist,HttpStatus.CREATED);

	}
	
	
	@GetMapping("/get/{technologyId}")
	public ResponseEntity<TechnoloyResponse> getTechnologyById(@PathVariable("technologyId")Integer technologyId)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());	
		TechnoloyResponse technoloyResponse = technoloyService.getTechnologyById(technologyId);
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<TechnoloyResponse>(technoloyResponse,HttpStatus.OK);

	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateTechnlogy(@Valid @RequestBody TechnologyCommentsRequest technologyCommentsRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());	
		String status = technoloyService.updateTechnology(technologyCommentsRequest);		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<String>(status,HttpStatus.OK);

	}
	
	@GetMapping("/generate/{format}")
	public void generateReports(@PathVariable("format") String format,HttpServletResponse response,HttpServletRequest request) {
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());	
		
		technoloyService.generateReports(format,response,request);
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		
		
	}
	
	@GetMapping("/dashboard/count/{userId}")
	public ResponseEntity<TechnologyStatusResponse> getDashBoardCount(@PathVariable("userId")Integer userId)
	{
	   logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());	
	   TechnologyStatusResponse technoloyResponse =  technoloyService.getDashBoardCount(userId);
	   logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
       return  new ResponseEntity<TechnologyStatusResponse>(technoloyResponse,HttpStatus.OK);
	}
	
	
}
