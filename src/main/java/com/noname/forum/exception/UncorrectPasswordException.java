package com.noname.forum.exception;

public class UncorrectPasswordException extends RuntimeException {
    public UncorrectPasswordException(String username) {
        super("Uncorrect password, user = " + username);
    }
}
