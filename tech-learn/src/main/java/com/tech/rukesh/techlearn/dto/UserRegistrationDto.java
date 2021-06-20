/**
 * 
 */
package com.tech.rukesh.techlearn.dto;

import javax.validation.constraints.Email;
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
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Data
@Builder
public class UserRegistrationDto {
 
	private Integer userId;
	@NotBlank(message ="firstName is required")
	private String firstName;
	@NotBlank(message ="lastName is required")
	private String lastName;
	@Email(message ="Email is not valid")
	@NotBlank(message ="email is required")
	private String email;
	private String password;
	
	
}
