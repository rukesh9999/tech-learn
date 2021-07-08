/**
 * 
 */
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
public class BatchJobSettingsResponse {

	private Integer portNo;
	
	private String protocal;
	
	private String host;
	
	private String userName;
	
	private String password; 
	
	private String submitter;
	
	
}
