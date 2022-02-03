/**
 * 
 */
package com.tech.rukesh.techlearn.dto;

import java.time.Instant;

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
@Data
public class AuthenticationResponse {

	private String authenticationToken;
	private String refreshToken;
	private Instant expiresAt;
	private String userName;
	
}
