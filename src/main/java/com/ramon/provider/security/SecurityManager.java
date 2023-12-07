package com.ramon.provider.security;

import com.ramon.provider.rs.entity.RSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityManager {

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected UserDetailsServiceImpl userDetailsService;

    public String authenticate(RSUser user) {
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return JWTUtils.generateToken(userDetails);
    }
}
