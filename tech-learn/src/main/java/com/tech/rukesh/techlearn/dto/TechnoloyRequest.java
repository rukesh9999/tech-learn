package com.tech.rukesh.techlearn.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TechnoloyRequest {
	private Integer id;
	private String  code;
	@NotBlank(message ="Name is Required")
	private String name;
	@NotBlank(message ="Description is Required")
	private String description;
	private Date createdDate;
	private Date modifiedDate;
	@JsonFormat(pattern ="dd-MM-yyyy HH:mm:ss")
   	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message ="expectedCompletionDate is required")
	private Date expectedCompletionDate;
	private String totalTimeToComplete;
	@NotNull(message ="StatusId is Required")
	private Integer statusId;
	@NotNull(message ="userId is required")
	private Integer userId;
	
	
}
