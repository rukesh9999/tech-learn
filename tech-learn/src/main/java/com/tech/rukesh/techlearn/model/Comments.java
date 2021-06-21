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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rukesh
 *
 */
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="comments",uniqueConstraints = {
@UniqueConstraint(columnNames ="id")
})
public class Comments {

	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE)
	@Column(name="id",unique =true,nullable=false,length=10)
	private Integer id;
	
	@Column(name="comment",unique =false,nullable=false,length=65536)
	private String comment;
	
	@Column(name="created_date",unique = false,nullable = false,length =100)
	private Date createdDate;
	
	@ManyToOne(targetEntity =UserRegistration.class,cascade =CascadeType.ALL,fetch =FetchType.EAGER)
	@JoinColumn(name ="createdBy",referencedColumnName ="user_id",unique =false ,nullable = false)
	private UserRegistration userRegistration;
	
	@ManyToOne(targetEntity=Technoloy.class,cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="technologyId",referencedColumnName ="id",unique =false ,nullable = false)
	private Technoloy technoloy;
	

}
