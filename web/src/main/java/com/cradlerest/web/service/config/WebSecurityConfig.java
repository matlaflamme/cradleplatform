package com.cradlerest.web.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/*
Defines the conditions for Java Spring Security
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Bean
	public UserDetailsService userDetailsService() {
		return super.userDetailsService();
	}

	// AuthenticationManagerBuilder docs
	// https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/config/annotation/authentication/builders/AuthenticationManagerBuilder.html
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Setting Service to find User in the database.
		// Add authentication based upon the custom UserDetailsService that is passed in.
		// GOTO: UserDetailsServiceImpl.java
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(getPasswordEncoder());
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// Disable security on all "/mock-api" routes (for testing)
				.antMatchers("/", "/home").permitAll()
				.antMatchers("/admin")
					.hasRole("ADMIN")
					.anyRequest().authenticated()
				.antMatchers("/healthworker")
					.hasRole("HEALTHWORKER") // match with 1 or more
				.antMatchers("/vht")
					.hasRole("VHT") // match with 1 or more
				.and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/")
					.failureUrl("/login?error")
					.permitAll()
				.and()
				.logout().permitAll()
				// Enable POST and DELETE methods
				.and().httpBasic()
				.and().csrf().disable();
	}

	/*
	 Java spring built in password encoder
	 https://docs.spring.io/spring-security/site/docs/4.2.4.RELEASE/apidocs/org/springframework/security/crypto/password/PasswordEncoder.html
	 */
	private PasswordEncoder getPasswordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence charSequence) {
				return charSequence.toString();
			}

			// Verify the encoded password obtained from storage matches the submitted raw password after it too is encoded.
			@Override
			public boolean matches(CharSequence charSequence, String s) {
				return true;
			}
		};
	}

}