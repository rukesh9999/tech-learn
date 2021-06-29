package com.tech.rukesh.techlearn.dto;

import javax.validation.constraints.NotBlank;

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
public class StatusMainRequest {

	@NotBlank(message ="Name is Required")
	private String name;
	
	@NotBlank(message ="Description is Required")
	private String description;
	
}
