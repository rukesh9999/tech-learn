/**
 * 
 */
package com.tech.rukesh.techlearn.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name ="refresh_token",uniqueConstraints = {
		@UniqueConstraint(columnNames ="id")
})
public class RefreshToken {
    @Id
    @GeneratedValue(strategy =GenerationType.SEQUENCE)
    @Column(name ="id",unique=true,nullable =false,length =10)
	private Integer id;
    
    @Column(name ="token",unique=false,nullable =false,length=65536)
	private String token;
    
    @Column(name ="created_date",unique=false,nullable=false,length =100)
	private Instant createddate;
	
}
