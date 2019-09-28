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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/*
 * Defines the conditions for Java Spring Security
 *
<<<<<<< HEAD
 * To secure a URI:
 * .permitAll() : permitAll
 * .anyRequest().authenticated() : any logged in user
 * .hasRole() : e.g. .hasRole("ADMIN")
 * .hasAnyRole : e.g. .hasAnyRole("ADMIN", "VHT").
 * NOTE: Order matters.
=======
 * use .antMatchers(Uri).hasRole(Role) to secure a uri
 *
 * use .antMatchers(Uri).hasAnyRole(Role,Role,Role) to secure a Uri for multiple users
 * Equivalent to user having multiple roles in database "Role" column.
>>>>>>> 872413e7d8354ad14b9dff598cf9d57c39efc421
 */
@Configuration
@EnableWebSecurity
@Component
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
				/*
				The order of the antMatchers is important
				E.g. if you permitAll "/", all consecutive URIs will be permitAll :)
				 */
				.antMatchers("/admin")
					.hasRole("ADMIN")
				.antMatchers("/healthworker")
					.hasRole("HEALTHWORKER")
				.antMatchers("/vht")
					.hasRole("VHT")
				.antMatchers("/api/**", "/upload_reading", "/upload").permitAll()
				.antMatchers("/").permitAll()
				.and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/")
					.failureUrl("/login?error")
					.permitAll()
				.and()
				.logout()
					.logoutSuccessUrl("/")
					.permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/accessDenied")
				// Enable POST and DELETE methods
				.and().httpBasic()
				.and().csrf().disable();
	}

//	/*
//	 Use this to ignore encoding (comment out the Bean below)
//	 https://docs.spring.io/spring-security/site/docs/4.2.4.RELEASE/apidocs/org/springframework/security/crypto/password/PasswordEncoder.html
//	 */
//	private PasswordEncoder getPasswordEncoder() {
//		return new PasswordEncoder() {
//			@Override
//			public String encode(CharSequence charSequence) {
//				return charSequence.toString();
//			}
//
//			// Verify the encoded password obtained from storage matches the submitted raw password after it too is encoded.
//			@Override
//			public boolean matches(CharSequence charSequence, String s) {
//				return true;
//			}
//		};
//	}
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}