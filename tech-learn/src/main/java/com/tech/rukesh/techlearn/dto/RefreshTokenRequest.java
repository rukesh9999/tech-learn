/**
 * 
 */
package com.tech.rukesh.techlearn.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rukesh
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Data
public class RefreshTokenRequest {

	@NotBlank(message ="refreshToken is required")
	private String refreshToken;
	
	@NotBlank(message ="refreshToken is required")
	private String userName;
	
}
