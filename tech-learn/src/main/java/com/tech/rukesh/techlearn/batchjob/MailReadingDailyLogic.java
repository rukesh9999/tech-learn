/**
 * 
 */
package com.tech.rukesh.techlearn.batchjob;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tech.rukesh.techlearn.controller.TechnoloyController;

/**
 * @author Rukesh
 *
 */
@Service
public class MailReadingDailyLogic {
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);

	
	
	

	
	Store store;
	// @Scheduled(fixedRate =5000)
	 public void connectToMailServer()
	 {
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		 Properties properties = new Properties();
		 Properties props = System.getProperties();
		 props.setProperty("mail.imap.host", "imap.gmail.com");
		 props.setProperty("mail.imap.port", "993");
		 props.setProperty("mail.imap.connectiontimeout", "5000");
		 props.setProperty("mail.imap.timeout", "5000");
		 
		 
		 Session emailsession = Session.getDefaultInstance(properties);
		 try {
		 store = emailsession.getStore("imaps");
		 store.connect("imap.googlemail.com", "noreply.techlearn@gmail.com", "techlearn123");
		 logger.info("Before connected to store");
		 //store.connect("smtp.gmail.com", "rukesh235@gmail.com", "rukesh235ece");
		 logger.info("After connected to store");

		
		 readMails();	
		 logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		 }catch (MessagingException e) {
			e.printStackTrace();
		}
	 }
	

	 
	 public void readMails() throws MessagingException
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
			 Address[] replyto = message.getReplyTo();
			 System.out.println("from====="+ from[i]);
			 System.out.println("replyto====="+ replyto[i]);
			 message.setFlag(Flags.Flag.SEEN, true);
		 }
			
		 logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());
 
		 emailFolder.close();
		 store.close();
	 }
	 
	 
	 
}
