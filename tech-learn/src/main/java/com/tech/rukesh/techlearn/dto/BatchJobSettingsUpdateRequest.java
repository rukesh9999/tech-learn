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
public class BatchJobSettingsUpdateRequest {

	@NotNull(message = "id is required")
	private Integer id;
	
	@NotNull(message = "portNo is required")
	private Integer portNo;
	
	@NotBlank(message = "protocal is required")
	private String protocal;
	
	@NotBlank(message = "host is required")
	private String host;
	
	@NotBlank(message = "userName is required")
	private String userName;
	
	@NotBlank(message = "password is required")
	private String password;
	
}
