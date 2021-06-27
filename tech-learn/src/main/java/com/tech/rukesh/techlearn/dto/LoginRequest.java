/**
 * 
 */
package com.tech.rukesh.techlearn.dto;

import javax.validation.constraints.NotBlank;

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
public class LoginRequest {
   
	@NotBlank(message ="userName cannot be null")
	private String userName;
	
	@NotBlank(message ="password cannot be null")
	private String password;
}
