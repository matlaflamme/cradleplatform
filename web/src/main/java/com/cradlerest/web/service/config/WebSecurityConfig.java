package com.cradlerest.web.service.config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


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
	/*
	The order of the antMatchers is important
	E.g. if you permitAll "/", all consecutive URIs will be permitAll :)
	*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic()
				.and()
				.authorizeRequests()
				.antMatchers("/admin")
					.hasRole("ADMIN")
				.antMatchers("/healthworker")
					.hasRole("HEALTHWORKER")
				.antMatchers("/vht")
					.hasRole("VHT")
//				// Disabling security on the following...
                .antMatchers("/api/**").permitAll()
				.antMatchers("/login*").permitAll()
				.antMatchers("/files/**").permitAll()
				.antMatchers("/home*").permitAll()
				.and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/")
					.failureUrl("/login?error")
					.successHandler(customAuthenticationSuccessHandler())
					.permitAll()
				.and()
				.logout()
					.logoutSuccessUrl("/")
					.permitAll()
				.and()
				.exceptionHandling()
					.accessDeniedPage("/error")
				// Enable POST and DELETE methods
				.and().csrf().disable();

	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}

//	@Bean
//	public FilterRegistrationBean twilioRequestValidatorFilter() {
//		FilterRegistrationBean  registration = new FilterRegistrationBean();
//		registration.setFilter(new TwilioRequestValidatorFilter());
//		registration.addUrlPatterns("/api/referral/send_referral_sms");
//		registration.setName("twilioFilter");
//		return registration;
//	}

}
