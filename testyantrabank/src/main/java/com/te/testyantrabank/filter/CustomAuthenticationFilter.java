package com.te.testyantrabank.filter;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.te.testyantrabank.dto.Customer;
import com.te.testyantrabank.exception.CustomAccessDeniedException;
import com.te.testyantrabank.security.JWTConfig;
import com.te.testyantrabank.service.CustomerServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private JWTConfig config;
	private CustomAccessDeniedException accessDenied;
	private CustomerServiceImpl serviceImpl;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JWTConfig config,
			CustomAccessDeniedException accessDenied,CustomerServiceImpl serviceImpl) {
		this.authenticationManager = authenticationManager;
		this.config = config;
		this.accessDenied = accessDenied;
		this.serviceImpl = serviceImpl;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		log.info("Username is " + username + " And Password Is " + password);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		Authentication authenticate = null;
		try {
			authenticate = authenticationManager.authenticate(authenticationToken);
		} catch (Exception exception) {
			try {
				log.error(exception.getMessage());
				accessDenied.handle(request, response, new AccessDeniedException(exception.getMessage()));
			} catch (Exception exception2) {
				System.out.println(exception2.getMessage());
			}
		}
		return authenticate;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
		String access_token = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + config.getAccess_token()))
				.withIssuer(request.getRequestURI().toString())
				.withClaim("roles",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);

		String refresh_token = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + config.getRefresh_token()))
				.withIssuer(request.getRequestURI().toString()).sign(algorithm);
		LinkedHashMap<String, Object> infomation = new LinkedHashMap<>();
		LinkedHashMap<String, Object> tokens = new LinkedHashMap<>();
		infomation.put("error", false); 
		infomation.put("message", "Successfully Login "+user.getUsername());
		tokens.put("access_token", access_token);
		tokens.put("refresh_token", refresh_token);
		infomation.put("data", tokens);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), infomation);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		String username = request.getParameter("username");
		Customer customer = serviceImpl.findByUserName(username);
		if(customer!=null)
			log.error("Enter The Correct Password!!!");
		else 
			log.error("User Name Does Not Exist!!!");
			
		super.unsuccessfulAuthentication(request, response, failed);
	}
}
