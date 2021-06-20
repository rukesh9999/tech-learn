/**
 * 
 */
package com.tech.rukesh.techlearn.model;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Data
@Entity
@Table(name = "mailacknowledgement",uniqueConstraints = {
 @UniqueConstraint(columnNames = "id")
})
public class MailAcknowledgement {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="id",unique=true,nullable=false,length =10)
	private Integer id;
	
	@Column(name ="from_address",unique=false,nullable=false,length=100)
	private String fromAddress;
	
	@Column(name = "to_address",unique=false,nullable=false,length=100)
	private String toAddress;
	
	@Column(name = "subject",unique=false,nullable=false,length=100)
	private String subject;
	
	@Column(name = "body",unique=false,nullable=false,length=65536)
	private String body;
	
	@Column(name = "typeOfMail",unique=false,nullable=false,length=100)
	private String typeOfMail;
	
	@ManyToOne(targetEntity=UserRegistration.class,cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="UserId",referencedColumnName = "user_id",unique =false,nullable =false)
	private UserRegistration userRegistration;
	

}
