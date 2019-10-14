package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.Omit;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class defines a user that has been authorized
 *
 * UserDetails interface provided by Spring Security
 * https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/core/userdetails/UserDetails.html
 */
@Omit
public class UserDetailsImpl extends User implements UserDetails {
    // Creates a new authorized user
    // You can reference this user through 'super' e.g. super.getUserId() etc..
    public UserDetailsImpl(final User user) {
        super(user);
    }

    /**
     * Returns list of authorities determined by user's roles
	 * A user may have more than 1 role.
     * Possible roles: HEALTHWORKER, VHT, ADMIN
     * Java spring security auto parses ROLE_ADMIN as ADMIN
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userRoles = super.getRoles();
        System.out.println("userRoles: " + userRoles);
        return Arrays
                .stream(userRoles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

	public String getPassword() {
        return super.getPassword();
    }

    // TODO: This really shouldn't always return true because people can leave the job but their account should still persist
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // TODO: This really shouldn't always return true because people can leave the job but their account should still persist
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // TODO: This really shouldn't always return true
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // TODO: This really shouldn't always return true
    @Override
    public boolean isEnabled() {
        return true;
    }
}
