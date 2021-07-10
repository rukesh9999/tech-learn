/**
 * 
 */
package com.tech.rukesh.techlearn.batchjob;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tech.rukesh.techlearn.controller.TechnoloyController;
import com.tech.rukesh.techlearn.dto.InboxMailsDto;
import com.tech.rukesh.techlearn.model.BatchJobSettings;
import com.tech.rukesh.techlearn.repository.BatchJobSettingsRepository;
import com.tech.rukesh.techlearn.service.InboxMailsService;
import com.tech.rukesh.techlearn.service.TechnoloyService;

/**
 * @author Rukesh
 *
 */
@Service
public class MailReadingDailyLogic {
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);
    
	@Autowired
	private InboxMailsService inboxMailsService;
	
	@Autowired
	private BatchJobSettingsRepository batchJobSettingsRepository;
	
	@Autowired
	private TechnoloyService technoloyService;
	
	
    //@Scheduled(fixedRate =5000)
	public void getAllBatchJobSettings()
	{
		List<BatchJobSettings>   listOfBatchJobSettings = (List<BatchJobSettings>) batchJobSettingsRepository.findAll();
		
		for(BatchJobSettings batchJobSettings : listOfBatchJobSettings)
		{
			  
			  this.connectToMailServer(batchJobSettings);
		}
		
	}

	
	
	
	 public void connectToMailServer(BatchJobSettings batchJobSettings)
	 {
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		 Properties properties = new Properties();
		 Properties props = System.getProperties();
		 props.setProperty("mail.imap.host", batchJobSettings.getHost());
		 props.setProperty("mail.imap.port", "993");
		 props.setProperty("mail.imap.connectiontimeout", "5000");
		 props.setProperty("mail.imap.timeout", "5000");
		 
		 
		 Session emailsession = Session.getDefaultInstance(properties);
		 try {
		 
		 Store store = emailsession.getStore(batchJobSettings.getProtocal());
		 logger.info("Before connected to store");		 
		 store.connect(batchJobSettings.getHost(), batchJobSettings.getUserName(), batchJobSettings.getPassword());	
		 logger.info("After connected to store");

		
		 this.readMails(store,batchJobSettings);	
		 logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		 }catch (MessagingException e) {
			e.printStackTrace();
		}
	 }
	

	 
	 public void readMails(Store store,BatchJobSettings batchJobSettings) throws MessagingException
	 {
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
		 Folder emailFolder  = store.getFolder("INBOX");
		 emailFolder.open(Folder.READ_WRITE);
		 int UnreadMessageCount =  emailFolder.getUnreadMessageCount();
		 System.out.println("UnreadMessageCount======"+UnreadMessageCount);

		 Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN),false));
		 
		 
		 for(int i=0;i<messages.length;i++)
		 {
			 Message message = messages[i];
			 System.out.println("==================message=="+i+"+==================");
			 System.out.println("subject======= "+message.getSubject());
			 System.out.println("description====="+ message.getDescription());
			
			 try {
				
				 Multipart multipart =  (Multipart) message.getContent();							  
				 System.out.println("content====="+ multipart.getBodyPart(0));
				 System.out.println("ContentType====="+ multipart.getContentType());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			 Address[] from = message.getFrom();
			
			 System.out.println("from====="+ from[i]);
			 
			 message.setFlag(Flags.Flag.SEEN, true);
			 
			 InboxMailsDto inboxMailsDto = new InboxMailsDto();
			 
			 inboxMailsDto.setConvertedToTechnology(false);
			 inboxMailsDto.setUserId(batchJobSettings.getUserRegistration().getUserId());
			 inboxMailsDto.setSubject(message.getSubject());
			 inboxMailsDto.setDescription(message.getSubject());
			 inboxMailsDto.setFromAddress(message.getFrom()[0].toString());
			 inboxMailsDto.setToAdddress(batchJobSettings.getUserName());
			 inboxMailsDto.setMailSentDate(message.getSentDate());
		
			 Integer inboxMailsId =  inboxMailsService.saveInboxMailsFromBatchJob(inboxMailsDto);
			 
			 inboxMailsDto.setId(inboxMailsId);
			 
			 if(batchJobSettings.getEnableAutoConvertToTechnology())
			 {
			    
				 technoloyService.saveTechnologyFromBatchJob(inboxMailsDto);
			 }
		 }
			
		 logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
 
		 emailFolder.close();
		 store.close();
	 }
	 
	 
	 
}
