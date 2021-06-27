/**
 * 
 */
package com.tech.rukesh.techlearn.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tech.rukesh.techlearn.controller.TechnoloyController;
import com.tech.rukesh.techlearn.repository.UserRegistrationRepository;
import com.tech.rukesh.techlearn.security.JWTProvider;



/**
 * @author Rukesh
 *
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JWTProvider JWTProvider;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	final static Logger logger = LoggerFactory.getLogger(TechnoloyController.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		
		String jwt = getJwtFromRequest(request);
		
		if(StringUtils.hasText(jwt) && JWTProvider.validateToken(jwt))
		{
			String username = JWTProvider.getUsernameFromJwt(jwt);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken
					(userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		filterChain.doFilter(request, response);
	}

	/**
	 * @param request
	 * @return
	 */
	private String getJwtFromRequest(HttpServletRequest request) {
		
		logger.info("Entered into ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		String bearerToken =  request.getHeader("Authorization");
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
		{
			return  bearerToken.substring(7);
		}
		
		logger.info("End of ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"... IN... "+this.getClass().getName());

		return bearerToken;
	}

	
}
