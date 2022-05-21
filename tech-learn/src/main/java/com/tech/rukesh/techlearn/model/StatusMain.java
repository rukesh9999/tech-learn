package com.tech.rukesh.techlearn.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import antlr.collections.List;
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
@Table(name = "statusmain",uniqueConstraints = {
@UniqueConstraint(columnNames = "id"),
@UniqueConstraint(columnNames = "name")
})
public class StatusMain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false,length = 50)
	private Integer id;
    
    @Column(name = "name",unique =true,nullable =false,length =150)
	private String name;
    
    
    @Lob
    @Column(name = "description",unique =false,nullable =false,length =65536)
    private String description;
    
    @Column(name="created_date",unique =false,nullable = false,length =150)
    private LocalDateTime createdDate;
    
    @Column(name="modified_date",unique =false,nullable = false,length =150)
    private LocalDateTime modifiedDate;
    
    
    
}
