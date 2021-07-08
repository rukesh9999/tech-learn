/**
 * 
 */
package com.tech.rukesh.techlearn.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.tech.rukesh.techlearn.controller.TechnoloyController;
import com.tech.rukesh.techlearn.exception.TechLearnException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author Rukesh
 *
 */

@Service
public class JWTProvider {

	private KeyStore KeyStore;

	@Value("${jwt.expiration.time}")
	private Long jwtExpirationInMillis;
	
	final static Logger logger = LoggerFactory.getLogger(JWTProvider.class);


	@PostConstruct
	public void init() {
		try {
			
			logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

			KeyStore = java.security.KeyStore.getInstance("JKS");
			logger.info("KeyStore ..."+KeyStore);
			 
			 File file =   ResourceUtils.getFile("classpath:springblog.jks");
			 
			 FileInputStream fileInputStream = new FileInputStream(file);
			 logger.info("inputStream ..."+fileInputStream);

			KeyStore.load(fileInputStream, "secret".toCharArray());
			
			logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public String generateToken(Authentication authentication) {
		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		User principle = (User) authentication.getPrincipal();
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return Jwts.builder().setSubject(principle.getUsername()).setIssuedAt(Date.from(Instant.now()))
				.signWith(getPrivateKey()).setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
				.compact();
	}
	

	public String generateTokenWithUserName(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(Date.from(Instant.now())).signWith(getPrivateKey())
				.setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis))).compact();
	}

	private PrivateKey getPrivateKey() {
		try {
			logger.info("KeyStore ..."+KeyStore);
			logger.info("getPrivateKey ..."+(PrivateKey) KeyStore.getKey("springblog", "secret".toCharArray()));
			return (PrivateKey) KeyStore.getKey("springblog", "secret".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
			throw new TechLearnException("Exception occured while retrieving public key from keystore");
		}
	}

	private PublicKey getPublickey() {
		try {
			return KeyStore.getCertificate("springblog").getPublicKey();
		} catch (KeyStoreException e) {
			throw new TechLearnException("Exception occured while " + "retrieving public key from keystore", e);
		}
	}

	public boolean validateToken(String jwt) {

		Jwts.parserBuilder().setSigningKey(getPublickey()).build().parseClaimsJws(jwt);
		return true;
	}

	public String getUsernameFromJwt(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getPublickey()).build().parseClaimsJws(token).getBody();

		return claims.getSubject();
	}


	/**
	 * @return the jwtExpirationInMillis
	 */
	public Long getJwtExpirationInMillis() {
		return jwtExpirationInMillis;
	}



	
	
	
	
	

}
