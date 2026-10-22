package com.noname.forum.exception.handler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.noname.forum.controller.AuthController;
import com.noname.forum.dto.error.ErrorResponse;
import com.noname.forum.exception.PostNotFoundException;
import com.noname.forum.exception.UserNotFoundException;
import com.noname.forum.exception.UsernameNotFoundException;

@ControllerAdvice 
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    @ExceptionHandler (Exception.class)
    public ResponseEntity<ErrorResponse> handlerAuth(Exception ex){
        
        log.error("Handle exeption, " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "exeption",
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    @ExceptionHandler (PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerPostNotFound(PostNotFoundException ex){
        log.error("PostNotFound exeption, " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "Post not found",
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } 

    @ExceptionHandler (UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerUserNotFound(UserNotFoundException ex){
        log.error("UserNotFound exeption, " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "User not found",
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } 

    @ExceptionHandler (UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerUsernameNotFound(UsernameNotFoundException ex){
        log.error("UsernameNotFound exeption, " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "User not found",
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } 

    
}
