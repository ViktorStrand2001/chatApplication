package com.viktor.chatApplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // man kan skapa variabler så här
    //@Column(unique = true) makes value Unique
    @NotEmpty
    @Size(min = 2, max = 64)
    private String username, password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean accountEnabled;
    private boolean credentialsNonExpired;

    @Transient // Do NOT persist through JPA (DB)
    @JsonIgnore // DO NOT add this attribute to API requests
    private List<GrantedAuthority> authority;

    public UserModel() {}

    public UserModel(String username, String password, List<GrantedAuthority> authority,
                      boolean accountNonExpired, boolean accountNonLocked,
                      boolean accountEnabled, boolean credentialsNonExpired) {
        this.username = username;
        this.password = password;
        this.authority = authority;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.accountEnabled = accountEnabled;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    // TODO - CHECK WITH ROLES
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority;
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
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return accountEnabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthority(List<GrantedAuthority> authority) {
        this.authority = authority;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setAccountEnabled(boolean accountEnabled) {
        this.accountEnabled = accountEnabled;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
}

