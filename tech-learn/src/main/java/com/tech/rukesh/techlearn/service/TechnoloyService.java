package com.tech.rukesh.techlearn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.tech.rukesh.techlearn.dto.InboxMailsDto;
import com.tech.rukesh.techlearn.dto.MailAcknowledgementDto;
import com.tech.rukesh.techlearn.dto.TechnologyCommentsRequest;
import com.tech.rukesh.techlearn.dto.TechnologyStatusResponse;
import com.tech.rukesh.techlearn.dto.TechnoloyRequest;
import com.tech.rukesh.techlearn.dto.TechnoloyResponse;
import com.tech.rukesh.techlearn.exception.BatchJobSettingsException;
import com.tech.rukesh.techlearn.exception.InboxMailException;
import com.tech.rukesh.techlearn.exception.InvalidFormatException;
import com.tech.rukesh.techlearn.exception.NoSuchBatchJobSettingsException;
import com.tech.rukesh.techlearn.exception.NoSuchInboxMailException;
import com.tech.rukesh.techlearn.exception.NoSuchStatusMainException;
import com.tech.rukesh.techlearn.exception.NoSuchTechnoloyExistsException;
import com.tech.rukesh.techlearn.exception.NoSuchUserExistsException;
import com.tech.rukesh.techlearn.exception.TechLearnException;
import com.tech.rukesh.techlearn.exception.TechnoloyAlreadyExistsException;
import com.tech.rukesh.techlearn.exception.TechnoloyException;
import com.tech.rukesh.techlearn.model.BatchJobSettings;
import com.tech.rukesh.techlearn.model.Comments;
import com.tech.rukesh.techlearn.model.InboxMails;
import com.tech.rukesh.techlearn.model.StatusMain;
import com.tech.rukesh.techlearn.model.Technoloy;
import com.tech.rukesh.techlearn.model.UserRegistration;
import com.tech.rukesh.techlearn.repository.BatchJobSettingsRepository;
import com.tech.rukesh.techlearn.repository.CommentsRepository;
import com.tech.rukesh.techlearn.repository.InboxMailsRepository;
import com.tech.rukesh.techlearn.repository.StatusMainRepository;
import com.tech.rukesh.techlearn.repository.TechnologyRepository;
import com.tech.rukesh.techlearn.repository.UserRegistrationRepository;
import com.tech.rukesh.techlearn.util.RandomCodeGenerator;
import com.tech.rukesh.techlearn.util.StatusMap;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;




@Transactional
@Service
@RequiredArgsConstructor
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
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private InboxMailsRepository inboxMailsRepository;
	
	@Value("${mail.fromAddress}")
	private String fromAddress;
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyService.class);
	
	/**
	 * @author Rukesh
	 * @param technoloyDto
	 * @return
	 */
	public String saveTechnology(TechnoloyRequest technoloyrequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		
		Optional<Technoloy> techopt = technologyRepository.findByName(technoloyrequest.getName());

		Technoloy techsave=null;
				
		if(techopt.isPresent())
		throw new TechnoloyAlreadyExistsException("TechnoloyAlreadyExists");
		else
		techsave = mapFromTechnoloyRequest(technoloyrequest);	
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
	public List<TechnoloyResponse> getAllTechnologies()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		List<Technoloy> techlist = (List<Technoloy>) technologyRepository.findAll();
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return techlist.stream().map(this::mapToTechnologyResponse).collect(Collectors.toList());

	}
	
	/**
	 * @author Rukesh
	 * @param id
	 * @return
	 */
	public TechnoloyResponse getTechnologyById(Integer id)
	{		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		Optional<Technoloy> techOpt =  technologyRepository.findById(id);
		if(!techOpt.isPresent())
		throw new NoSuchTechnoloyExistsException(" NoSuchTechnoloyExists");
		else
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return mapToTechnologyResponse(techOpt.get());
		
	}
	
	
	/**
	 * @author Rukesh
	 * @param technoloyDto
	 * @return
	 */
	public String updateTechnology(TechnologyCommentsRequest technologyCommentsRequest)
	{		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());		
		Technoloy  techUpdate = mapFromTechnologyCommentsRequest(technologyCommentsRequest);
		String Status="";
		try {
			techUpdate = technologyRepository.save(techUpdate);	
		}catch (Exception e) {
			throw new TechnoloyException(e.getMessage());
		}
		
		
		Comments comments = new Comments();
		comments.setComment(technologyCommentsRequest.getComment());
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
	private Technoloy mapFromTechnologyCommentsRequest(TechnologyCommentsRequest technologyCommentsRequest) {
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());			
		StatusMain statusMain=null;
		
		TechnoloyResponse TechnoloyResponse = getTechnologyById(technologyCommentsRequest.getId());
		
		if(technologyCommentsRequest.getStatusId()!=null) {
		 Optional<StatusMain> statusMainopt  = statusMainRepository.findById(technologyCommentsRequest.getStatusId());
		 statusMain = statusMainopt.get();
		}else {
		    statusMain = statusMainRepository.findByName(TechnoloyResponse.getStatusName()).orElseThrow(()->new NoSuchStatusMainException("status doesnot exists"));
			
		}
		
		UserRegistration userRegistration = authenticationService.getCurrentUser();
		
		String totaltimeString = calculateTotalTime(TechnoloyResponse.getCreatedDate(),technologyCommentsRequest.getExpectedCompletionDate());	
       	
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());		

		return Technoloy.builder()
				.id(technologyCommentsRequest.getId())
				.code(TechnoloyResponse.getCode())
				.name(technologyCommentsRequest.getName())
				.description(technologyCommentsRequest.getDescription())
				.createdDate(TechnoloyResponse.getCreatedDate())
				.expectedCompletionDate(technologyCommentsRequest.getExpectedCompletionDate())
				.totalTimeToComplete(totaltimeString)
				.modifiedDate(LocalDateTime.now())
				.statusMain(statusMain)
				.userRegistration(userRegistration)
		        .build();	
	   
	}


	/**
    * @author Rukesh
    * @param techsave
    * @return
    */
	public TechnoloyResponse mapToTechnologyResponse(Technoloy techsave) {
	
	  	StatusMain statusmain = statusMainRepository.findById(techsave.getStatusMain().getId()).orElseThrow(()->new NoSuchStatusMainException("No such status exists"));
	  	
	  	UserRegistration userRegistration =  userRegistrationRepository.findById(techsave.getUserRegistration().getUserId()).orElseThrow(()->new NoSuchUserExistsException("user doesnot exits"));
	  		  	 
	  	String firstName = userRegistration.getFirstName();
	  	String lastName = userRegistration.getLastName();
		String fullName = firstName+lastName;
		
		return TechnoloyResponse.builder()
				.name(techsave.getName())
				.code(techsave.getCode())
				.createdDate(techsave.getCreatedDate())
				.modifiedDate(techsave.getModifiedDate())
				.description(techsave.getDescription())
				.expectedCompletionDate(techsave.getExpectedCompletionDate())
				.totalTimeToComplete(techsave.getTotalTimeToComplete())
				.statusName(statusmain.getName())
				.CreatedBy(fullName)
				.build();
	}


	/**
	 * @author Rukesh
	 * @param technoloyDto
	 * @return
	 */
	public Technoloy mapFromTechnoloyRequest(TechnoloyRequest technoloyRequest) {
		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());		
	
		LocalDateTime createdDate =LocalDateTime.now();
		LocalDateTime modifieddate = LocalDateTime.now();
		LocalDateTime ExpectedCompletionDate  =  technoloyRequest.getExpectedCompletionDate();
		String totaltimeString = calculateTotalTime(createdDate,ExpectedCompletionDate);
		
		UserRegistration userRegistration =   authenticationService.getCurrentUser();
		//Optional<UserRegistration> userOptional =  auth.findById(technoloyRequest.getUserId());
		
		Optional<StatusMain> statusMainOpt  = statusMainRepository.findById(StatusMap.New);	
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());		

		return Technoloy.builder()
				.name(technoloyRequest.getName())
				.description(technoloyRequest.getDescription())
				.code(randomCodeGenerator.generateRandomCode())
				.createdDate(createdDate)
				.modifiedDate(modifieddate)
				.expectedCompletionDate(ExpectedCompletionDate)
				.totalTimeToComplete(totaltimeString)
				.userRegistration(userRegistration)
				.statusMain(statusMainOpt.get())
				.build();
				
				
	}
	
	
	

	private String calculateTotalTime(LocalDateTime startDate , LocalDateTime endDate) {
		
		 logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		 
		 //LocalDate startdate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		 //LocalDate ExpectedCompletionDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		 long years = ChronoUnit.YEARS.between(startDate, endDate);
	     long months = ChronoUnit.MONTHS.between(startDate, endDate);
	     long days = ChronoUnit.DAYS.between(startDate, endDate);
	     
		// Period period =  Period.between(startdate, ExpectedCompletionDate);
		// int years = period.getYears();
		// int months = period.getMonths();
		// int days = period.getDays();
	     
		 String yearsappend="";
		 String Monthsappend="";
		 String Daysappend="";
		 String finalappender="";
		 if(years!=0)
		 {
			 if(years==1)
			 yearsappend+=years+" Year";
			 else
			 yearsappend+=years+" Years";	 
		 }
		 
		 if(months!=0)
		 {
			   if(months==1)
			   Monthsappend+=months+" Month";
			   else
			   Monthsappend+=months+" Months";	
		 }
		 
		 if(days!=0)
		 {
			 if(days==1)
			 Daysappend+=days+" Day";
			 else
			 Daysappend+=days+" Days";
				 
			 
		 }
		 
		 finalappender=yearsappend+" "+Monthsappend+" "+Daysappend;
		 
		 
		
		
	   logger.info("totalDaysStr...."+finalappender);

	   logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return finalappender;
	}
	
	

	public void generateReports(String format,HttpServletResponse response,HttpServletRequest request) {
		
		
		 JasperPrint jasperPrint=null;
		 String fileName="listoftechnologies";
		 String folderName="";
		 String finalPath="";
		 
		try {
			
			String path = request.getContextPath();
			folderName=path.trim()+"/exportdoc/".trim();
			File destinationFolder =new File(folderName);
			if(!destinationFolder.exists()) {
			destinationFolder.mkdir();	
			}else {
			  File[] files = destinationFolder.listFiles();
			  for(File file: files)
			  {
				 file.delete(); 
			  }
			}
			
			finalPath+=folderName.trim()+fileName.trim()+".".trim()+format.trim();
			
			
			List<TechnoloyResponse> listoftechnologies  =  getAllTechnologies();
			File file =  ResourceUtils.getFile("classpath:technologies.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(listoftechnologies);
            Map<String, Object> parameters  = new HashMap<>();
            parameters.put("createdBy", "rukesh");
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);
            
               if(format.equalsIgnoreCase("pdf")) {
            	   
        		response.setHeader("CONTENT_DISPOSITION", "attachment;filename=technologiesreport."+format);
        		response.setContentType("application/pdf");
        		
        		JasperExportManager.exportReportToPdfFile(jasperPrint, finalPath);;
        		
        		}else if(format.equalsIgnoreCase("csv"))
        		{
        			response.setHeader("CONTENT_DISPOSITION", "attachment;filename=technologiesreport."+format);
        			response.setContentType("application/x-csv");
        			JRCsvExporter exporter = new JRCsvExporter();
     			    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
     			    exporter.setExporterOutput(new SimpleWriterExporterOutput(finalPath.toString()));
     			    SimpleCsvExporterConfiguration configuration = new SimpleCsvExporterConfiguration();
     			    //configuration.setWriteBOM(Boolean.TRUE);
     			    exporter.setConfiguration(configuration);
     			    exporter.exportReport();
        		}
        		else if(format.equalsIgnoreCase("doc") || format.equalsIgnoreCase("docx"))
        		{
        			    
        			response.setHeader("CONTENT_DISPOSITION", "attachment;filename=technologiesreport."+format);
        			response.setContentType("application/msword");
        			JRDocxExporter exporter = new JRDocxExporter();
        		    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));      
        		   // File exportReportFile = new File(fileName + ".docx");
        		    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(finalPath.toString()));
        		    exporter.exportReport();
        			
        			
        		}else if(format.equalsIgnoreCase("xls") || format.equalsIgnoreCase("xlsx"))
        		{
        			
        			JRXlsxExporter exporter = new JRXlsxExporter();
		            SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
		            reportConfigXLS.setSheetNames(new String[] { "sheet1" });
		            exporter.setConfiguration(reportConfigXLS);
		            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(finalPath.toString()));
		            exporter.exportReport();
        			response.setHeader("CONTENT_DISPOSITION", "attachment;filename=technologiesreport."+format);
        			response.setContentType("application/ms-excel");
        			
        		}
        		else if(format.equalsIgnoreCase("xml"))
        		{
        			response.setHeader("CONTENT_DISPOSITION", "attachment;filename=technologiesreport."+format);
        			response.setContentType("application/xml");
        			JasperExportManager.exportReportToXmlFile(jasperPrint, finalPath,false);
        		}
        		else if(format.equalsIgnoreCase("html"))
        		{
        			response.setHeader("CONTENT_DISPOSITION", "attachment;filename=technologiesreport."+format);
        			response.setContentType("application/TEXT_HTML");
        			JasperExportManager.exportReportToHtmlFile(jasperPrint, finalPath);
        		}else {
        			
        			throw new InvalidFormatException("InvalidFormatException");
        		}
               
                FileInputStream fileInputStream =new FileInputStream(new File(finalPath));
                
                byte[] bytes =new byte[16384];
                int size=0;
                while((size=fileInputStream.read(bytes))!=-1)
                {
                	response.getOutputStream().write(bytes, 0, size);
                }
                
                
               
               
		} catch (JRException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}


	public TechnologyStatusResponse getDashBoardCount(Integer userId) {
		
		Integer newStatusCount = technologyRepository.countBystatusMainIdAndUserRegistrationUserId(StatusMap.New,userId);
		Integer inProgressStatusCount = technologyRepository.countBystatusMainIdAndUserRegistrationUserId(StatusMap.InProgress,userId);
		Integer closedStatusCount = technologyRepository.countBystatusMainIdAndUserRegistrationUserId(StatusMap.Closed,userId);
		
		TechnologyStatusResponse technologyStatusResponse =new TechnologyStatusResponse();
		technologyStatusResponse.setClosedStatusCount(closedStatusCount);
		technologyStatusResponse.setInProgressStatusCount(inProgressStatusCount);
		technologyStatusResponse.setNewStatusCount(newStatusCount);

		 return technologyStatusResponse;
	}


	
	 public void saveTechnologyFromBatchJob(InboxMailsDto inboxMailsDto)
	 {
		 
		 Technoloy technoloy =  mapFromTechnoloyRequestFromBatchJob(inboxMailsDto);
		 
		 try {
			    technologyRepository.save(technoloy);
		     }catch (Exception e) {
			   throw new TechnoloyException(e.getMessage());
		    }
		     
		    InboxMails   inboxMails =   inboxMailsRepository.findById(inboxMailsDto.getId())
		       .orElseThrow(()->new NoSuchInboxMailException("No Such InboxMail Exists"));
		 
		    inboxMails.setConvertedToTechnology(true);
		    
		    try {
		       inboxMailsRepository.save(inboxMails);
		    }catch (Exception e) {
		    	 throw new InboxMailException(e.getMessage());
			}
		    
		    MailAcknowledgementDto mailAcknowledgementDto = new MailAcknowledgementDto();
			mailAcknowledgementDto.setUserId(technoloy.getUserRegistration().getUserId());
			mailAcknowledgementDto.setToAddress(technoloy.getUserRegistration().getEmail());
			mailAcknowledgementDto.setTypeOfMail("TechnologyCreation");
			mailAcknowledgementDto.setSubject("New Technology Created");
			mailAcknowledgementDto.setFromAddress(fromAddress);
			
			
			mailManagerService.sendTechnologyAcknowledgeRelatedMail(mailAcknowledgementDto,technoloy);
		 
	 }
	 
	 
	 
    public Technoloy mapFromTechnoloyRequestFromBatchJob(InboxMailsDto inboxMailsDto) {
    	
    	  Optional<UserRegistration> userRegistrationOpt  =  userRegistrationRepository.findById(inboxMailsDto.getUserId());
		  if(!userRegistrationOpt.isPresent())
	      throw new NoSuchUserExistsException("User does not exists");
		  
		  LocalDateTime createdDate = LocalDateTime.now();
		  
		  LocalDateTime  modifiedDate = LocalDateTime.now();
		  
//		  Calendar calender = Calendar.getInstance();
//		  calender.setTime(createdDate);
//		  calender.add(Calendar.DAY_OF_MONTH,31);
//		  Date  expectedCompletionDate =  calender.getTime();
		  LocalDateTime  expectedCompletionDate = LocalDateTime.now().plusDays(31);

		  String timeToComplete = calculateTotalTime(createdDate,expectedCompletionDate);
		  
	      Optional<StatusMain> statusMainOpt  = statusMainRepository.findById(StatusMap.New);	
			
	      return Technoloy.builder()
	    		  .name(inboxMailsDto.getSubject())
	    		  .description(inboxMailsDto.getDescription())
	    		  .code(randomCodeGenerator.generateRandomCode())
	    		  .createdDate(createdDate)
	    		  .modifiedDate(modifiedDate)
	    		  .expectedCompletionDate(expectedCompletionDate)
	    		  .totalTimeToComplete(timeToComplete)
	    		  .statusMain(statusMainOpt.get())
	    		  .userRegistration(userRegistrationOpt.get())
	    		  .build();
		     
    }
	
	
}
