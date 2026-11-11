package com.noname.forum.exception;

public class UnvalidTokenException extends RuntimeException{
    public UnvalidTokenException() {
        super("Token unvalid");
    }
}
