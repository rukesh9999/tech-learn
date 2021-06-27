/**
 * 
 */
package com.tech.rukesh.techlearn.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tech.rukesh.techlearn.exception.NoSuchRefreshTokenException;
import com.tech.rukesh.techlearn.model.RefreshToken;
import com.tech.rukesh.techlearn.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author Rukesh
 *
 */

@RequiredArgsConstructor
@Transactional
@Service
public class RefreshTokenService {

	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	
	public RefreshToken generateRefreshToken()
	{
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreateddate(Instant.now());
		return refreshTokenRepository.save(refreshToken);
	}
	
	
	public void validateRefreshToken(String token)
	{
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(()-> new NoSuchRefreshTokenException("Invalid refreshToken "));
	}
	
	
	public void deleteToken(String token)
	{
		refreshTokenRepository.deleteByToken(token);
	}
	
}
