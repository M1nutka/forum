package com.noname.forum.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User not found, userId = " + userId);
    }

    public UserNotFoundException(String username) {
        super("User not found, username = " + username);
    }
}