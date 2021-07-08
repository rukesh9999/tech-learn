/**
 * 
 */
package com.tech.rukesh.techlearn.dto;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tech.rukesh.techlearn.model.UserRegistration;

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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Data
public class InboxMailsResponse {

	private String subject;
	
	private String description;
	
	private String fromAddress;
	
	private String toAdddress;
	
	private Date mailSentDate;
	
	private boolean autoConvertToTechnology;
	
	private String mailSentBy;
	
}
