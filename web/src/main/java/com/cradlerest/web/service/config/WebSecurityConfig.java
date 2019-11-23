package com.cradlerest.web.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/*
 * Defines the conditions for securing our application/service
 *
 * To secure a URI:
 * .permitAll() : permitAll
 * .anyRequest().authenticated() : any logged in user
 * .hasRole() : e.g. .hasRole("ADMIN")
 * .hasAnyRole : e.g. .hasAnyRole("ADMIN", "VHT").
 * NOTE: Order matters.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private UserDetailsService userDetailsService;

	public WebSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

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
	/*
	The order of the antMatchers is important
	E.g. if you permitAll "/", all consecutive URIs will be permitAll :)
	*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic()
				.and()

				// Login configuration
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.failureUrl("/login?error")
				.successHandler(customAuthenticationSuccessHandler())
				.permitAll()
				.and()

				// Logout configuration
				.logout()
				.logoutSuccessUrl("/")
				.permitAll()
				.and()

				//
				// API endpoint authentication
				//

				.authorizeRequests()

				// Open endpoints
				.antMatchers("/api/user/whoami").authenticated()
				.antMatchers("/api/twilio/**").hasRole("ADMIN")
				// Admin only endpoints
				.antMatchers("/api/hc/**").hasRole("ADMIN")
				.antMatchers("/api/internal/**").hasRole("ADMIN")
				.antMatchers("/api/stats/**").hasRole("ADMIN")
				.antMatchers("/api/twilio/**").hasRole("ADMIN")
				.antMatchers("/api/user/**").hasRole("ADMIN")

				// VHT/Health Worker endpoints
				.antMatchers("/api/patient/**").hasAnyRole("HEALTHWORKER", "VHT")
				.antMatchers("/api/reading/**").hasAnyRole("HEALTHWORKER", "VHT")

				// VHT only endpoints
				.antMatchers("/api/referral/send/**").hasRole("VHT")

				// Health worker only endpoints
				.regexMatchers("/api/referral(?:/.+)?/all").hasRole("HEALTHWORKER")

				// Deny any other request
				.antMatchers("/api/**").denyAll()
				.and()

				// Exception handling
				.exceptionHandling()
				.accessDeniedPage("/error")
				.and()

				// Enable POST and DELETE methods
				.csrf()
				.disable();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}

}
