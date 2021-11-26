package com.te.testyantrabank.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.te.testyantrabank.dto.Customer;
import com.te.testyantrabank.message.Message;
import com.te.testyantrabank.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/customer")
@Api(value = "Customer",tags = {"Customer Operations"})
public class CustomerController {
	@Autowired
	private CustomerService customerService;

	@ApiOperation(value = "Withdraw")
	@PutMapping(path = "/withdraw/{amount}")
	public ResponseEntity<Message> withdraw(@PathVariable("amount") double amount) {
		log.info(amount + "Amount Successfully Withdraw  ");
		return new ResponseEntity<Message>(customerService.withdraw(amount), HttpStatus.OK);
	}

	@ApiOperation(value = "Deposit")
	@PutMapping(path = "/deposit/{amount}")
	public ResponseEntity<Message> deposite(@PathVariable("amount") double amount) {
		log.info(amount + "Amount Successfully Deposited  ");
		return new ResponseEntity<Message>(customerService.deposit(amount), HttpStatus.OK);
	}

	@ApiOperation(value = "View Balance")
	@GetMapping("/balance")
	public ResponseEntity<Message> getBalance() {
		return new ResponseEntity<Message>(customerService.getBalance(), HttpStatus.OK);
	}

	
	@ApiOperation(value = "Token Referesh")
	@GetMapping("/token/refresh")
	public void refreashToken(HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		String header = request.getHeader(AUTHORIZATION);
		if (header != null && header.startsWith("Bearer ")) {
			try {
				String refresh_token = header.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				Customer customer = customerService.findByUserName(username);
				String access_token = JWT.create().withSubject(customer.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURI().toString())
						.withClaim("roles", new ArrayList<>().add(new SimpleGrantedAuthority("USER"))).sign(algorithm);
				LinkedHashMap<String, Object> message = new LinkedHashMap<>();
				LinkedHashMap<String, Object> tokens = new LinkedHashMap<>();
				message.put("error", false);
				message.put("message", "Sucessfully Generated Access token");
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				message.put("data", tokens);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), message);
			} catch (Exception exception) {
				response.setHeader("error", exception.getMessage());
				response.setStatus(FORBIDDEN.value());
				HashMap<String, String> error = new HashMap<>();
				error.put("error", exception.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		} else
			throw new RuntimeException("Refresh token is missing");

	}
}

