package com.ramon.provider.security;

import com.ramon.provider.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserAuth implements UserDetails {

    public UserAuth(User user) {
        username = user.getUsername();
        password = user.getPassword();
        authorities = user.getPuesto() == User.PUESTO.PROFESOR ? List.of(new Authority(Authority.Auth.WRITE), new Authority(Authority.Auth.READ)) : List.of(new Authority(Authority.Auth.READ));
        role = user.getPuesto().name();
        email = "sfj";
    }

    protected String username;
    protected String password;
    protected List<Authority> authorities;
    protected boolean expired;
    protected boolean locked;
    protected String role;
    protected String email;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(authorities);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return !locked;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public boolean isExpired() {
        return expired;
    }

    public boolean isLocked() {
        return locked;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public class Authority implements GrantedAuthority {

        enum Auth {
            READ,
            WRITE
        }

        protected Auth authority;


        public Authority(Auth authority) {
            this.authority = authority;
        }

        @Override
        public String getAuthority() {
            return authority.name();
        }
    }
}
