package com.viktor.chatApplication.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Roles {
    ADMIN("GET_POST_PUT_DELETE"),
    USER("GET");

    public String getPermissions() {
        return permissions;
    }

    private final String permissions;

    Roles(String permissions) {
        this.permissions = permissions;
    }

    public List<GrantedAuthority> splitPermissions() {
        String[] permissionsArray = permissions.split("_");

        System.out.println("THIS IS SPLIT: " + Arrays.stream(permissionsArray)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));

        return Arrays.stream(permissionsArray)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public List<GrantedAuthority> getAuthorities(){

        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + name());
        List<GrantedAuthority> permissions = new ArrayList<>();

        permissions.add(role);
        permissions.addAll(splitPermissions());

        System.out.println("THIS IS HETAUTH: " + permissions);

        return permissions;
    }

}

