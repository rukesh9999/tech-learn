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
@Table(name ="batch_job_settings")
public class BatchJobSettings {

	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE)
	@Column(name ="id",unique =true,nullable =false)
	private Integer id;
	
	@Column(name ="port_no",unique =false,nullable =false)
	private Integer portNo;
	
	@Column(name ="protocal",unique =false,nullable =false)
	private String protocal;
	
	@Column(name ="host",unique =false,nullable =false)
	private String host;
	
	@Column(name ="user_name",unique =false,nullable =false)
	private String userName;
	
	@Column(name ="password",unique =false,nullable =false)
	private String password;
	
	@Column(name ="enable_autoConvert_to_technology",unique=false,nullable =false)
	private Boolean  enableAutoConvertToTechnology;
	
	@ManyToOne(targetEntity =UserRegistration.class,cascade =CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name ="userId",referencedColumnName ="user_id",unique =false ,nullable = false)
	private UserRegistration userRegistration;
	
	
}
