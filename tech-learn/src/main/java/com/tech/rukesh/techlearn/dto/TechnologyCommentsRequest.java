/**
 * 
 */
package com.tech.rukesh.techlearn.dto;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyCommentsRequest {
	
	@NotNull(message ="id is Required")
	private Integer id;
	
	@NotBlank(message ="Name is Required")
	private String name;
	
	@NotBlank(message ="Description is Required")
	private String description;
	
	@JsonFormat(pattern ="dd-MM-yyyy HH:mm:ss")
   	@Temporal(TemporalType.TIMESTAMP)
	@Future(message ="expectedCompletionDate must be future Date")
	@NotNull(message ="expectedCompletionDate is required")
	private LocalDateTime expectedCompletionDate;
	
	@NotBlank(message ="comment is required")
	private String comment;
	
	private Integer statusId;

}
