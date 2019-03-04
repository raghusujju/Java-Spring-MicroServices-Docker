package com.qantas.proj.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final String API_PATH_PATTERN = "/qantas/userapp/**";
	
	@Value("${qantas.security.test.user}")
	private String testUser;
	
	@Value("${qantas.security.test.password}")
	private String testPassword;
	
	@Value("${qantas.security.admin.user}")
	private String adminUser;
	
	@Value("${qantas.security.admin.user}")
	private String adminPassword;
	
	
	@Override
 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 		auth.inMemoryAuthentication().withUser(testUser).password("{noop}"+testPassword).roles("USER").and()
 		.withUser(adminUser).password("{noop}"+adminPassword).roles("ADMIN");
 	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests().antMatchers(API_PATH_PATTERN).fullyAuthenticated().and().httpBasic();
		http.csrf().disable();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
	}
}
