package com.tech.rukesh.techlearn.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusMainUpdateRequest {

	@NotNull(message ="id is Required")
	private Integer id;
	
	@NotBlank(message ="Name is Required")
	private String name;
	
	@NotBlank(message ="Description is Required")
	private String description;
	
}
