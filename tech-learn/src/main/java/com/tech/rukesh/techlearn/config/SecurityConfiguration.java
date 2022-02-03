/**
 * 
 */
package com.tech.rukesh.techlearn.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tech.rukesh.techlearn.security.JwtAuthenticationFilter;


/**
 * @author Rukesh
 *
 */

@EnableWebSecurity
@Configuration
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encode());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
		 http.exceptionHandling().authenticationEntryPoint((request,response,exception)->{response.sendError(
				 HttpServletResponse.SC_UNAUTHORIZED,exception.getMessage());
			     }).and();
		 http.authorizeRequests()
		 .antMatchers("/api/auth/login").permitAll()
		 .antMatchers("/api/auth/register").permitAll()
		 .antMatchers("/api/auth/verifyUserExistsORNot/*").permitAll()
		 .antMatchers("/api/auth/forgotpassword/*").permitAll()
		 .antMatchers("/api/auth/refresh/token").permitAll()
		 .antMatchers("/api/auth/changepassword/*").permitAll()
		 .antMatchers("/v2/api-docs",
                 "/configuration/ui",
                 "/swagger-resources/**",
                 "/configuration/security",
                 "/swagger-ui.html",
                 "/webjars/**")
         .permitAll()
		 .anyRequest().authenticated();
		  http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	
		
	}

	
	@Bean
	public PasswordEncoder encode()
	{
		return new BCryptPasswordEncoder();
	}

	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}
	
	
}
