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

import com.tech.rukesh.techlearn.dto.StatusMainDto;
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
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyService.class);
	
	/**
	 * @author Rukesh
	 * @param statusMainDto
	 * @return
	 */
	public StatusMainDto saveStatusMain(StatusMainDto statusMainDto)
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		StatusMain statusMainsave=null;
		Optional<StatusMain> optstatusmain = statusMainRepository.findByName(statusMainDto.getName());
		if(optstatusmain.isPresent())
		throw new StatusMainException("StatusMainAlreadyExists");
		else	
		statusMainsave = mapFromDto(statusMainDto);
		try {
		statusMainRepository.save(statusMainsave);
		}catch (Exception e) {
			throw new StatusMainException(e.getMessage());
		}
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return mapToDto(statusMainsave);
	}
	
	/**
	 * @author Rukesh
	 * @return
	 */
	public List<StatusMainDto> getAllStatusMain()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		List<StatusMain> statusmainlist = (List<StatusMain>) statusMainRepository.findAll();
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		return statusmainlist.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}
	
	
	/**
	 * @author Rukesh
	 * @param id
	 * @return
	 */
    public StatusMainDto getStatusMainById(Integer id)
    {
	   logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
       Optional<StatusMain> statusmain = statusMainRepository.findById(id);
       if(!statusmain.isPresent())
       throw new NoSuchStatusMainException("NoSuchStatusMain Exists");
       else
   	   logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
       return mapToDto(statusmain.get());
	    
    }

    /**
     * 
     * @param statusMain
     * @return
     */
    public StatusMainDto updateStatusMain(StatusMainDto statusMainUpdateDto)
    {
 	    logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
    	StatusMainDto statusmaindbdto  = getStatusMainById(statusMainUpdateDto.getId());
    	Optional<StatusMain> optstatusmain = statusMainRepository.findByName(statusMainUpdateDto.getName());
    	if(optstatusmain.isPresent())
    	throw new StatusMainAlreadyExists("StatusMainAlreadyExists");
    	StatusMain statusMain=new StatusMain();
    	statusMain.setId(statusmaindbdto.getId());
    	statusMain.setName(statusMainUpdateDto.getName());
    	statusMain.setDescription(statusMainUpdateDto.getDescription());
    	statusMain.setCreatedDate(statusmaindbdto.getCreatedDate());
    	statusMain.setModifiedDate(new Date(System.currentTimeMillis()));
    	try {
    		statusMain = statusMainRepository.save(statusMain);
    	}catch (Exception e) {
			throw new StatusMainException(e.getMessage());
		}
 	    logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
    	return mapToDto(statusMain);
    }
    
    
	public StatusMain mapFromDto(StatusMainDto statusMainDto) {				
		
		return StatusMain.builder()
				.name(statusMainDto.getName())
				.description(statusMainDto.getDescription())
				.createdDate(new Date(System.currentTimeMillis()))
				.modifiedDate(new Date(System.currentTimeMillis()))
				.build();
	}



	public StatusMainDto  mapToDto(StatusMain statusMain) {
		
		return StatusMainDto.builder()
			   .id(statusMain.getId())
			   .name(statusMain.getName())
			   .description(statusMain.getDescription())
			   .createdDate(statusMain.getCreatedDate())
			   .modifiedDate(statusMain.getModifiedDate())
			   .build();
			   
	}
	
}
