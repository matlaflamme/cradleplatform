package com.cradlerest.web.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 This class defines a user that has been authorized

 UserDetails interface provided by Spring Security
 https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/core/userdetails/UserDetails.html
 */
public class UserDetailsImpl extends User implements UserDetails {

    // Creates a new authorized user
    // You can reference this user through 'super'
    public UserDetailsImpl(final User user) {
        super(user);
    }
    /*
     A user may have more role (I guess?)
     For now they only have one:
     HEALTHWORKER
     VHT
     ADMIN
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + super.getRole()));
        return grantedAuthorities;
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
