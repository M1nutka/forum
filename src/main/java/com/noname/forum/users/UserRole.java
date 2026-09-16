package com.noname.forum.users;

import org.springframework.security.core.GrantedAuthority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole  implements GrantedAuthority{
    USER("USER"),
    ADMIN("ADMIN");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }
}
