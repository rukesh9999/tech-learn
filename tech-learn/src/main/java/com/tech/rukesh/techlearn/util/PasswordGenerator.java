/**
 * 
 */
package com.tech.rukesh.techlearn.util;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tech.rukesh.techlearn.controller.TechnoloyController;

/**
 * @author Rukesh
 *
 */
@Component
public class PasswordGenerator {

	
	public PasswordGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);
	
	public String generatePassword()
	{
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		final int passwordlength=6;
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String values = Capital_chars + Small_chars +
                numbers ;
        
        Random random =new Random();
        
        StringBuffer password =new StringBuffer();
        
        for(int index=0;index<passwordlength;index++)
        {
        	password.append(values.charAt(random.nextInt(60)));
        }
		logger.info("Password is ..."+password.toString());
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return password.toString();
	}

	
	
}
