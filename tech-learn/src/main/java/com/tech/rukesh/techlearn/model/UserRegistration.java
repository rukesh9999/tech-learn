/**
 * 
 */
package com.tech.rukesh.techlearn.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_registration",uniqueConstraints = {
@UniqueConstraint(columnNames ="user_id"),
@UniqueConstraint(columnNames = "email")
})
public class UserRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name ="user_id",unique =true,nullable =false,length =10)
	private Integer userId;
	@Column(name ="first_name",unique =false,nullable=false,length =100)
	private String firstName;
	@Column(name ="last_name",unique =false,nullable=false,length =100)
	private String lastName;
	@Column(name ="email",unique =true,nullable=false,length =100)
	private String email;
	@Column(name ="password",unique=true,nullable=false,length =100)
	private String password;
	
	
	
	
}
