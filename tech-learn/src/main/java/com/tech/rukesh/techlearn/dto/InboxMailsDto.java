/**
 * 
 */
package com.tech.rukesh.techlearn.dto;

import java.util.Date;

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
public class InboxMailsDto {

	private String subject;
	
	private String description;
	
	private String fromAddress;
	
	private String toAdddress;
	
	private Date mailSentDate;
	
	private boolean autoConvertToTechnology;
	
	private Integer UserId;
}
