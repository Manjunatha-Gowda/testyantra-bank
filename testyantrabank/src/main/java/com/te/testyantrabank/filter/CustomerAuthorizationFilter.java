package com.te.testyantrabank.filter;


import static java.util.Arrays.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.te.testyantrabank.exception.CustomAccessDeniedException;
import com.te.testyantrabank.service.CustomerServiceImpl;;

public class CustomerAuthorizationFilter extends OncePerRequestFilter {

	private CustomAccessDeniedException accessDenied;
	private CustomerServiceImpl serviceImpl;
	public CustomerAuthorizationFilter(CustomAccessDeniedException accessDenied,CustomerServiceImpl serviceImpl) {
		this.accessDenied = accessDenied;
		this.serviceImpl = serviceImpl;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getServletPath().equals("/api/v1/login")
				|| request.getServletPath().equals("/api/v1/customer/token/refresh")) {
			filterChain.doFilter(request, response);
		} else {
			String header = request.getHeader("Authorization");
			if (header != null && header.startsWith("Bearer ")) {
				try {
					String token = header.substring(7);
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(token);
					String username = decodedJWT.getSubject();
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					if(!roles[0].equals("ADMIN"))
					if (!serviceImpl.getCustomer().getUserName().equals(username)) {
						try {
							accessDenied.handle(request, response, new AccessDeniedException("Unauthorized Access Token"));
						} catch (Exception exception2) {
							System.out.println(exception2.getMessage());
						}
					}
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				} catch (Exception exception) {
					try {
						accessDenied.handle(request, response, new AccessDeniedException(exception.getMessage()));
					} catch (Exception exception2) {
						System.out.println(exception2.getMessage());
					}
				}
			} else
				filterChain.doFilter(request, response);
		}
	}

}
