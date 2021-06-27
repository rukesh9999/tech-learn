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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tech.rukesh.techlearn.dto.TechnologyCommentsDto;
import com.tech.rukesh.techlearn.dto.TechnoloyRequest;
import com.tech.rukesh.techlearn.service.TechnoloyService;

import lombok.RequiredArgsConstructor;

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
	public ResponseEntity<List<TechnoloyRequest>> getAllTechnologies()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		List<TechnoloyRequest>  techlist = technoloyService.getAllTechnologies();
		logger.info("End of..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<List<TechnoloyRequest>>(techlist,HttpStatus.OK);

	}
	
	
	@GetMapping("/get/{id}")
	public ResponseEntity<TechnoloyRequest> getTechnologyById(@PathVariable("id")Integer id)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());	
		TechnoloyRequest technologyDto = technoloyService.getTechnologyById(id);
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<TechnoloyRequest>(technologyDto,HttpStatus.OK);

	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateTechnlogy(@Valid @RequestBody TechnologyCommentsDto technologyCommentsDto)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());	
		String status = technoloyService.updateTechnology(technologyCommentsDto);		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return new ResponseEntity<String>(status,HttpStatus.OK);

	}
	
	@GetMapping("/generate/{format}")
	public void generateReports(@PathVariable("format") String format,HttpServletResponse response,HttpServletRequest request) {
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());	
		
		technoloyService.generateReports(format,response,request);
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		
		
	}
	
}
