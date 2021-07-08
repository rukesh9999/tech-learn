package com.tech.rukesh.techlearn.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tech.rukesh.techlearn.dto.StatusMainRequest;
import com.tech.rukesh.techlearn.dto.StatusMainResponse;
import com.tech.rukesh.techlearn.dto.StatusMainUpdateRequest;
import com.tech.rukesh.techlearn.exception.NoSuchStatusMainException;
import com.tech.rukesh.techlearn.exception.StatusMainAlreadyExists;
import com.tech.rukesh.techlearn.exception.StatusMainException;
import com.tech.rukesh.techlearn.model.StatusMain;
import com.tech.rukesh.techlearn.repository.StatusMainRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
@Transactional
public class StatusMainService {

	@Autowired
	private StatusMainRepository statusMainRepository;
	
	final static Logger logger = LoggerFactory.getLogger(StatusMainService.class);
	
	/**
	 * @author Rukesh
	 * @param statusMainDto
	 * @return
	 */
	public String saveStatusMain(StatusMainRequest statusMainRequest)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		StatusMain statusMainsave=null;
		Optional<StatusMain> optstatusmain = statusMainRepository.findByName(statusMainRequest.getName());
		if(optstatusmain.isPresent())
		throw new StatusMainException("StatusMainAlreadyExists");
		else	
		statusMainsave = mapFromStatusMainRequest(statusMainRequest);
		try {
		statusMainRepository.save(statusMainsave);
		}catch (Exception e) {
			throw new StatusMainException(e.getMessage());
		}
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return "StatusMain saved successfully";
	}
	
	/**
	 * @author Rukesh
	 * @return
	 */
	public List<StatusMainResponse> getAllStatusMain()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		List<StatusMain> statusmainlist = (List<StatusMain>) statusMainRepository.findAll();
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return statusmainlist.stream()
				.map(this::mapToStatusMainResponse)
				.collect(Collectors.toList());
	}
	
	
	/**
	 * @author Rukesh
	 * @param id
	 * @return
	 */
    public StatusMainResponse getStatusMainById(Integer id)
    {
	   logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
       Optional<StatusMain> statusmain = statusMainRepository.findById(id);
       if(!statusmain.isPresent())
       throw new NoSuchStatusMainException("NoSuchStatusMain Exists");
       else
   	   logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
       return mapToStatusMainResponse(statusmain.get());
	    
    }

    /**
     * 
     * @param statusMain
     * @return
     */
    public String updateStatusMain(StatusMainUpdateRequest statusMainUpdateRequest)
    {
 	    logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
    	StatusMainResponse statusmaindbdto  = getStatusMainById(statusMainUpdateRequest.getId());
    	
    	StatusMain statusMain=new StatusMain();
    	statusMain.setId(statusMainUpdateRequest.getId());
    	statusMain.setName(statusMainUpdateRequest.getName());
    	statusMain.setDescription(statusMainUpdateRequest.getDescription());
    	statusMain.setCreatedDate(statusmaindbdto.getCreatedDate());
    	statusMain.setModifiedDate(new Date(System.currentTimeMillis()));
    	try {
    		statusMain = statusMainRepository.save(statusMain);
    	}catch (Exception e) {
			throw new StatusMainException(e.getMessage());
		}
 	    logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
    	return "status updated successfully";
    }
    
    
	public StatusMain mapFromStatusMainRequest(StatusMainRequest statusMainRequest) {				
		
		return StatusMain.builder()
				.name(statusMainRequest.getName())
				.description(statusMainRequest.getDescription())
				.createdDate(new Date(System.currentTimeMillis()))
				.modifiedDate(new Date(System.currentTimeMillis()))
				.build();
	}



	public StatusMainResponse  mapToStatusMainResponse(StatusMain statusMain) {
		
		return StatusMainResponse.builder()
			   .name(statusMain.getName())
			   .description(statusMain.getDescription())
			   .createdDate(statusMain.getCreatedDate())
			   .modifiedDate(statusMain.getModifiedDate())
			   .build();
			   
	}
	
}
