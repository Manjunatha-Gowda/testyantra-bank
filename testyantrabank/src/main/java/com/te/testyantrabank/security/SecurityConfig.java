
package com.te.testyantrabank.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.te.testyantrabank.exception.CustomAccessDeniedException;
import com.te.testyantrabank.exception.CustomAuthenticationEntryPoint;
import com.te.testyantrabank.filter.CustomAuthenticationFilter;
import com.te.testyantrabank.filter.CustomerAuthorizationFilter;
import com.te.testyantrabank.service.CustomerServiceImpl;

@Configuration

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private CustomerServiceImpl  serviceImpl ;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CustomAuthenticationFilter authenticattionFilter = new CustomAuthenticationFilter(authenticationManagerBean(),
				config(), new CustomAccessDeniedException(), serviceImpl);
		CustomerAuthorizationFilter authorizationFilter = new CustomerAuthorizationFilter(
				new CustomAccessDeniedException(), serviceImpl);
		authenticattionFilter.setFilterProcessesUrl("/api/v1/login");
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests()
				.antMatchers("/api/v1/login/**", "/api/v1/customer/token/refresh/**",
						"/swagger-resources/configuration/ui", "/swagger-resources",
						"/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**")
				.permitAll();
		http.authorizeRequests().antMatchers("/api/v1/customer/**").hasAnyAuthority("USER");
		http.authorizeRequests().antMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN");
		http.authorizeRequests().anyRequest().authenticated();
		http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
		http.addFilter(authenticattionFilter);
		http.addFilterBefore(authorizationFilter, CustomAuthenticationFilter.class);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


	@Bean
	public JWTConfig config() {
		return new JWTConfig();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources",
				"/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**");
	}
}
