package com.tech.rukesh.techlearn.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.tech.rukesh.techlearn.dto.TechnoloyDto;
import com.tech.rukesh.techlearn.exception.NoSuchTechnoloyExistsException;
import com.tech.rukesh.techlearn.exception.TechnoloyAlreadyExistsException;
import com.tech.rukesh.techlearn.exception.TechnoloyException;
import com.tech.rukesh.techlearn.model.StatusMain;
import com.tech.rukesh.techlearn.model.Technoloy;
import com.tech.rukesh.techlearn.repository.StatusMainRepository;
import com.tech.rukesh.techlearn.repository.TechnologyRepository;
import com.tech.rukesh.techlearn.util.RandomCodeGenerator;


@Transactional
@Service
public class TechnoloyService {

	@Autowired
	private TechnologyRepository technologyRepository;
	
	@Autowired
	private RandomCodeGenerator randomCodeGenerator;
	
	@Autowired
	private StatusMainRepository statusMainRepository;
	
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyService.class);
	
	/**
	 * @author Rukesh
	 * @param technoloyDto
	 * @return
	 */
	public TechnoloyDto saveTechnology(TechnoloyDto technoloyDto)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		Technoloy techsave=null;
		Optional<Technoloy> techopt = technologyRepository.findByName(technoloyDto.getName());
		if(techopt.isPresent())
		throw new TechnoloyAlreadyExistsException("TechnoloyAlreadyExists");
		else
		techsave = mapFromDto(technoloyDto);
		try {
		technologyRepository.save(techsave);
		}catch (Exception e) {
		  throw new TechnoloyException(e.getMessage());
		}
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return mapToDto(techsave);
	}
	
	
	/**
	 * @author Rukesh
	 * @return
	 */
	public List<TechnoloyDto> getAllTechnologies()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		List<Technoloy> techlist = (List<Technoloy>) technologyRepository.findAll();
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return techlist.stream().map(this::mapToDto).collect(Collectors.toList());

	}
	
	/**
	 * @author Rukesh
	 * @param id
	 * @return
	 */
	public TechnoloyDto getTechnologyById(Integer id)
	{		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		Optional<Technoloy> techOpt =  technologyRepository.findById(id);
		if(!techOpt.isPresent())
		throw new NoSuchTechnoloyExistsException(" NoSuchTechnoloyExists");
		else
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return mapToDto(techOpt.get());
		
	}
	
	
	/**
	 * @author Rukesh
	 * @param technoloyDto
	 * @return
	 */
	public TechnoloyDto updateTechnology(TechnoloyDto technoloyUpdateDto)
	{		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		TechnoloyDto TechnoloyDtoDB = getTechnologyById(technoloyUpdateDto.getId());
		Optional<StatusMain> statusMain  = statusMainRepository.findById(technoloyUpdateDto.getStatusId());	
		
		Technoloy technology =new Technoloy();
		technology.setId(TechnoloyDtoDB.getId());
		technology.setStatusMain(statusMain.get());
		technology.setModifiedDate(new Date(System.currentTimeMillis()));
		technology.setCode(TechnoloyDtoDB.getCode());
		technology.setCreatedDate(TechnoloyDtoDB.getCreatedDate());
		technology.setName(TechnoloyDtoDB.getName());
		technology.setDescription(TechnoloyDtoDB.getDescription());
		try {
		technology = technologyRepository.save(technology);	
		}catch (Exception e) {
			throw new TechnoloyException(e.getMessage());
		}
		logger.info(" End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return mapToDto(technology);

	}

   
	/**
    * @author Rukesh
    * @param techsave
    * @return
    */
	public TechnoloyDto mapToDto(Technoloy techsave) {
	
		return TechnoloyDto.builder()
				.id(techsave.getId())
				.name(techsave.getName())
				.code(techsave.getCode())
				.createdDate(techsave.getCreatedDate())
				.modifiedDate(techsave.getModifiedDate())
				.description(techsave.getDescription())
				.expectedCompletionDate(techsave.getExpectedCompletionDate())
				.totalTimeToComplete(techsave.getTotalTimeToComplete())
				.statusId(techsave.getStatusMain().getId())
				.build();
	}


	/**
	 * @author Rukesh
	 * @param technoloyDto
	 * @return
	 */
	public Technoloy mapFromDto(TechnoloyDto technoloyDto) {
		
		Optional<StatusMain> statusMain  = statusMainRepository.findById(technoloyDto.getStatusId());		
		calculateTotalTime(technoloyDto);
		return Technoloy.builder()
				.name(technoloyDto.getName())
				.description(technoloyDto.getDescription())
				.statusMain(statusMain.get())
				.createdDate(new Date(System.currentTimeMillis()))
				.modifiedDate(new Date(System.currentTimeMillis()))
				.expectedCompletionDate(technoloyDto.getExpectedCompletionDate())
				.totalTimeToComplete(calculateTotalTime(technoloyDto))
				.code(randomCodeGenerator.generateRandomCode()).build();
				
				
	}


	/**
	 * @param technoloyDto
	 */
	private String calculateTotalTime(TechnoloyDto technoloyDto) {
		 logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		 LocalDate now = new Date(System.currentTimeMillis()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		 LocalDate ExpectedCompletionDate =  technoloyDto.getExpectedCompletionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		 double totalDays = (double) ChronoUnit.DAYS.between(now,ExpectedCompletionDate);
		 String totalDaysStr="";
		 if(totalDays>366) {
			 totalDays = totalDays/366;
			 DecimalFormat f = new DecimalFormat("##.0");
			 f.format(totalDays);
			 if(totalDays==366)
			 totalDaysStr+=totalDays+" Year";
			 else
			 totalDaysStr+=totalDays+" Years";
			 
		 }
		 else if(totalDays>=31 && totalDays<=366)
		 {
			 totalDays = totalDays/31;
			 DecimalFormat f = new DecimalFormat("##.0");
			 f.format(totalDays);
			 if(totalDays==1)
			 totalDaysStr+=totalDays+" Month";
			 else
			 totalDaysStr+=totalDays+" Months";
			
		 }else {
			 
			 if(totalDays==1)
			 totalDaysStr+=totalDays+" Day";
			 else
			 totalDaysStr+=totalDays+" Days";
				 
		 }
		
	   logger.info("totalDaysStr...."+totalDaysStr);

	   logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return totalDaysStr;
	}


	
	 
	
	
}
