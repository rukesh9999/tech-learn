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
@ToString
@Getter
@Setter
@Data
public class AuthenticationResponse {

	private String athenticationToken;
	private String refreshToken;
	private Instant expiresAt;
	private String userName;
	
}
