package com.cradlerest.web.service;

import com.cradlerest.web.model.User;
import com.cradlerest.web.model.UserDetailsImpl;
import com.cradlerest.web.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

/*
 * UserDetails handler between database
 * Relies on class: UserDetailsImpl
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    // Returns list of all users found in database with 'name'
    // TODO: Restrict usernames to be unique
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));

        System.out.println("username: " + username);
        // Creates the authenticated user(s)
        UserDetails authorizedUserDetails = userOptional.map(UserDetailsImpl::new).get();

        // Will throw exception if user detail is invalid (locked, expired, disabled, credentials expired)
        // https://github.com/spring-projects/spring-security/blob/master/core/src/main/java/org/springframework/security/authentication/AccountStatusUserDetailsChecker.java
        new AccountStatusUserDetailsChecker().check(authorizedUserDetails);

        return authorizedUserDetails;
    }
}
