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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Builder
@Data
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="technology",uniqueConstraints = {
 @UniqueConstraint(columnNames = "id"),
 @UniqueConstraint(columnNames = "code"),
 @UniqueConstraint(columnNames = "name")
})
public class Technoloy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id",unique =true,nullable =false,length =10)
	private Integer id;
    
    @Column(name="code",unique =true,nullable =false,length = 100)
	private String  code;
    
    @Column(name="name",unique =true,nullable =false,length =100)
	private String  name;
    
    @Column(name = "created_date",unique =false,nullable = false,length =100)
    private Date createdDate;
    
    @Column(name = "modified_date",unique =false,nullable = false,length =100)
    private Date modifiedDate;
    
    @Lob
    @Column(name="description",unique = false,nullable =false,length =65536)   
    private String description;
    
	
    @Column(name ="expected_completion_date",unique = false,nullable =false,length =100)
    private Date expectedCompletionDate;
    
   
    @Column(name="total_time_to_complete",unique = false,nullable =false,length =50)
    private String totalTimeToComplete;
    
    @ManyToOne(targetEntity=StatusMain.class,cascade =CascadeType.ALL,fetch = FetchType.EAGER)  
    @JoinColumn(name="statusId",referencedColumnName = "id")
    private StatusMain statusMain;
    
    @ManyToOne(targetEntity=UserRegistration.class,cascade =CascadeType.ALL,fetch =FetchType.EAGER)
    @JoinColumn(name="CreatedBy",referencedColumnName ="user_id")
    private UserRegistration userRegistration;
    
    
	
}
