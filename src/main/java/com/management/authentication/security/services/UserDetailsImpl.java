package com.management.authentication.security.services;

import com.management.authentication.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String password;

    private Collection<? extends GrantedAuthority> authories;

    public UserDetailsImpl(Long id, String username, String password, List<GrantedAuthority> authorities) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.authories = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }



    public static UserDetailsImpl build (User user) {
        List<GrantedAuthority>authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), authorities);
    }


}
