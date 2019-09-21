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
User handler for database

 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> usersOptional = userRepository.findByUsername(name);

        if (usersOptional == null) {
            throw new UsernameNotFoundException("User '" + name + "' not found");
        }

        // Creates the authenticated user(s)
        UserDetails authorizedUserDetails = new UserDetailsImpl(usersOptional.get());

        // Will throw exception if user detail is invalid (locked, expired, disabled, credentials expired)
        // https://github.com/spring-projects/spring-security/blob/master/core/src/main/java/org/springframework/security/authentication/AccountStatusUserDetailsChecker.java
        new AccountStatusUserDetailsChecker().check(authorizedUserDetails);

        return authorizedUserDetails;
    }
}
