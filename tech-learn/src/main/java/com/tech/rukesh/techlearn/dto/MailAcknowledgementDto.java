/**
 * 
 */
package com.tech.rukesh.techlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rukesh
 *
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Data
public class MailAcknowledgementDto {

	
	private Integer id;	
	private String fromAddress;	
	private String toAddress;	
	private String subject;	
	private String body;
	private String typeOfMail;		
	private Integer UserId;
	

}
