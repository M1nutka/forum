package com.noname.forum.exception;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String id) {
        super("Role not found id = " + id);
    }
}
