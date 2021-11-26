package com.te.testyantrabank.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class JWTConfig {

	@Autowired
	private Environment environment;
	@SuppressWarnings("unused")
	private long access_token;
	@SuppressWarnings("unused")
	private long refresh_token;

	public long getAccess_token() {
		return Long.parseLong(environment.getProperty("access.token"));
	}

	public long getRefresh_token() {
		return Long.parseLong(environment.getProperty("refresh.token"));
	}
}
