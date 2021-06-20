package com.tech.rukesh.techlearn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tech.rukesh.techlearn.repository.TechnologyRepository;
@Component
public class RandomCodeGenerator {

   @Autowired
   private TechnologyRepository technologyRepository;
	
   final static Logger logger = LoggerFactory.getLogger(RandomCodeGenerator.class);
   
	public String generateRandomCode()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		int count=0;
		String code = technologyRepository.getTechnologyCodeById();
		if(code!=null && !"".equals(code) && !"null".equalsIgnoreCase(code)) {
		 count = Integer.parseInt(code.substring(code.length()-1));
		 code=code.substring(0,code.length()-1);
		 code+=count+1;	
		 logger.info("code..."+code);

		}		
		else {
		code="21-"+count;
		logger.info("code..."+code);
		}
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return code;
	}
}
