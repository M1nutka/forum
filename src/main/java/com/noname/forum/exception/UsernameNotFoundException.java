package com.noname.forum.exception;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String username){
        super("User not found Exception, where username = " + username);
    }
}
