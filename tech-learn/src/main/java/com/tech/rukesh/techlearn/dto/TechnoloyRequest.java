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
	
	@NotBlank(message ="Name is Required")
	private String name;
	
	@NotBlank(message ="Description is Required")
	private String description;
	
	
	@Future(message = "expectedCompletionDate must be Future Date")
	@NotNull(message ="expectedCompletionDate is required")
	private LocalDateTime expectedCompletionDate;
	
	
}
