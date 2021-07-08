/**
 * 
 */
package com.tech.rukesh.techlearn.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name="inbox_mails",uniqueConstraints = {
	@UniqueConstraint(columnNames ="id")	
})
public class InboxMails {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE )
	@Column(name ="id",unique =true,nullable =false)
	private Integer id;
	
	@Column(name ="subject",unique =false,nullable =false)
	private String subject;
	
	@Column(name ="description",unique =false,nullable =false)
	private String description;
	
	@Column(name ="from_address",unique =false,nullable =false)
	private String fromAddress;
	
	@Column(name ="to_adddress",unique =false,nullable =false)
	private String toAdddress;
	
	@Column(name ="mail_sent_date",unique =false,nullable =false)
	private Date mailSentDate;
	
	@Column(name ="auto_convert_to_technology",unique =false,nullable =false)
	private boolean autoConvertToTechnology;
	
	@ManyToOne(targetEntity=UserRegistration.class,cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name ="userId",referencedColumnName ="user_id")
	private UserRegistration userRegistration;
		
}
