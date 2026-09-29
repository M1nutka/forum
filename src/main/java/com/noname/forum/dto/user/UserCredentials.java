package com.noname.forum.dto.user;

public record UserCredentials(
    Long id,
    String username,
    String passwordHash
) {

}
