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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.tech.rukesh.techlearn.dto.MailAcknowledgementDto;
import com.tech.rukesh.techlearn.dto.TechnologyCommentsDto;
import com.tech.rukesh.techlearn.dto.TechnoloyDto;
import com.tech.rukesh.techlearn.exception.NoSuchTechnoloyExistsException;
import com.tech.rukesh.techlearn.exception.TechnoloyAlreadyExistsException;
import com.tech.rukesh.techlearn.exception.TechnoloyException;
import com.tech.rukesh.techlearn.model.Comments;
import com.tech.rukesh.techlearn.model.StatusMain;
import com.tech.rukesh.techlearn.model.Technoloy;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.CommentsRepository;
import com.tech.rukesh.techlearn.repository.StatusMainRepository;
import com.tech.rukesh.techlearn.repository.TechnologyRepository;
import com.tech.rukesh.techlearn.repository.UserRegistrationRepository;
import com.tech.rukesh.techlearn.util.RandomCodeGenerator;
import com.tech.rukesh.techlearn.util.StatusMap;

import jdk.jshell.spi.ExecutionControl.UserException;




@Transactional
@Service
public class TechnoloyService {

	@Autowired
	private TechnologyRepository technologyRepository;
	
	@Autowired
	private RandomCodeGenerator randomCodeGenerator;
	
	@Autowired
	private StatusMainRepository statusMainRepository;
	
	@Autowired
	private CommentsRepository  commentsRepository;
	
	@Autowired
	private UserRegistrationRepository userRegistrationRepository;
	
	@Autowired
	private MailManagerService mailManagerService;
	
	
	
	@Value("${mail.fromAddress}")
	private String fromAddress;
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyService.class);
	
	/**
	 * @author Rukesh
	 * @param technoloyDto
	 * @return
	 */
	public String saveTechnology(TechnoloyDto technoloyDto)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		
		Optional<Technoloy> techopt = technologyRepository.findByName(technoloyDto.getName());

		Technoloy techsave=null;
				
		if(techopt.isPresent())
		throw new TechnoloyAlreadyExistsException("TechnoloyAlreadyExists");
		else
		techsave = mapFromTechnoloyDto(technoloyDto);	
		try {
		technologyRepository.save(techsave);
		}catch (Exception e) {
		  throw new TechnoloyException(e.getMessage());
		}
		
		
		MailAcknowledgementDto mailAcknowledgementDto = new MailAcknowledgementDto();
		mailAcknowledgementDto.setUserId(techsave.getUserRegistration().getUserId());
		mailAcknowledgementDto.setToAddress(techsave.getUserRegistration().getEmail());
		mailAcknowledgementDto.setTypeOfMail("TechnologyCreation");
		mailAcknowledgementDto.setSubject("New Technology Created");
		mailAcknowledgementDto.setFromAddress(fromAddress);
		
		mailManagerService.sendTechnologyAcknowledgeRelatedMail(mailAcknowledgementDto,techsave);
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return "technology saved Successfully";
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
	public String updateTechnology(TechnologyCommentsDto TechnologyCommentsUpdateDto)
	{		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());		
		Technoloy  techUpdate = mapFromTechnologyCommentsDto(TechnologyCommentsUpdateDto);
		String Status="";
		try {
			techUpdate = technologyRepository.save(techUpdate);	
		}catch (Exception e) {
			throw new TechnoloyException(e.getMessage());
		}
		
		
		Comments comments = new Comments();
		comments.setComment(TechnologyCommentsUpdateDto.getComment());
		comments.setCreatedDate(new Date(System.currentTimeMillis()));
		comments.setTechnoloy(techUpdate);
		comments.setUserRegistration(techUpdate.getUserRegistration());
		
		MailAcknowledgementDto mailAcknowledgementDto = new MailAcknowledgementDto();
		mailAcknowledgementDto.setUserId(techUpdate.getUserRegistration().getUserId());
		mailAcknowledgementDto.setToAddress(techUpdate.getUserRegistration().getEmail());
	    if(techUpdate.getStatusMain().getId()==StatusMap.Closed) {
		mailAcknowledgementDto.setTypeOfMail("TechnologyClosed");
		mailAcknowledgementDto.setSubject("Technology Closed");
		Status = "technology Closed Successfully";
	    }else {
	    	mailAcknowledgementDto.setTypeOfMail("TechnologyUpdated");
			mailAcknowledgementDto.setSubject("Technology Updated");
			Status = "technology updated Successfully";
	    }
		mailAcknowledgementDto.setFromAddress(fromAddress);
		
		mailManagerService.sendTechnologyAcknowledgeRelatedMail(mailAcknowledgementDto,techUpdate);

		
		try {
		  commentsRepository.save(comments);
		}catch (Exception e) {
			throw new TechnoloyException(e.getMessage());
		}
		
		logger.info(" End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return Status;

	}

   
	/**
	 * @param technologyCommentsUpdateDto
	 * @return
	 */
	private Technoloy mapFromTechnologyCommentsDto(TechnologyCommentsDto technologyCommentsUpdateDto) {
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());			
		
		TechnoloyDto TechnoloyDtoDB = getTechnologyById(technologyCommentsUpdateDto.getId());
		Optional<StatusMain> statusMain  = statusMainRepository.findById(technologyCommentsUpdateDto.getStatusId());	
		
		Optional<UserRegistration> userOptional =  userRegistrationRepository.findById(TechnoloyDtoDB.getUserId());
		
		String totaltimeString = calculateTotalTime(TechnoloyDtoDB.getCreatedDate(),technologyCommentsUpdateDto.getExpectedCompletionDate());	
       	
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());		

		return Technoloy.builder()
				.id(TechnoloyDtoDB.getId())
				.code(TechnoloyDtoDB.getCode())
				.name(technologyCommentsUpdateDto.getName())
				.description(technologyCommentsUpdateDto.getDescription())
				.createdDate(TechnoloyDtoDB.getCreatedDate())
				.expectedCompletionDate(technologyCommentsUpdateDto.getExpectedCompletionDate())
				.totalTimeToComplete(totaltimeString)
				.modifiedDate(new Date(System.currentTimeMillis()))
				.statusMain(statusMain.get())
				.userRegistration(userOptional.get())
		        .build();	
	   
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
				.userId(techsave.getUserRegistration().getUserId())
				.build();
	}


	/**
	 * @author Rukesh
	 * @param technoloyDto
	 * @return
	 */
	public Technoloy mapFromTechnoloyDto(TechnoloyDto technoloyDto) {
		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());		
	
		Date createdDate =new Date(System.currentTimeMillis());
		Date modifieddate = new Date(System.currentTimeMillis());
		Date ExpectedCompletionDate  =  technoloyDto.getExpectedCompletionDate();
		String totaltimeString = calculateTotalTime(createdDate,ExpectedCompletionDate);
		
		Optional<UserRegistration> userOptional =  userRegistrationRepository.findById(technoloyDto.getUserId());
		
		Optional<StatusMain> statusMainOpt  = statusMainRepository.findById(technoloyDto.getStatusId());	
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());		

		return Technoloy.builder()
				.name(technoloyDto.getName())
				.description(technoloyDto.getDescription())
				.code(randomCodeGenerator.generateRandomCode())
				.createdDate(createdDate)
				.modifiedDate(modifieddate)
				.expectedCompletionDate(ExpectedCompletionDate)
				.totalTimeToComplete(totaltimeString)
				.userRegistration(userOptional.get())
				.statusMain(statusMainOpt.get())
				.build();
				
				
	}
	
	
	


	/**
	 * @param technoloyDto
	 */
	private String calculateTotalTime(Date startDate , Date endDate) {
		 logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		 LocalDate startdate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		 LocalDate ExpectedCompletionDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		 double totalDays = (double) ChronoUnit.DAYS.between(startdate,ExpectedCompletionDate);
		 String totalDaysStr="";
		 if(totalDays>365) {
			 totalDays = totalDays/366;
			 DecimalFormat f = new DecimalFormat("#.#");
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
