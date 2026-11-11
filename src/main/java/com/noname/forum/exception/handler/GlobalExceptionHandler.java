package com.noname.forum.exception.handler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.noname.forum.controller.AuthController;
import com.noname.forum.dto.error.ErrorResponse;
import com.noname.forum.exception.PostNotFoundException;
import com.noname.forum.exception.RoleNotFoundException;
import com.noname.forum.exception.UncorrectPasswordException;
import com.noname.forum.exception.UnvalidTokenException;
import com.noname.forum.exception.UserNotFoundException;

@ControllerAdvice 
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    @ExceptionHandler (Exception.class)
    public ResponseEntity<ErrorResponse> handlerAuth(Exception ex){
        
        log.error("Exception, " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "exception",
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    @ExceptionHandler (PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerPostNotFound(PostNotFoundException ex){
        log.error("Post not found, " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "post_not_found",
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } 

    @ExceptionHandler (UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerUserNotFound(UserNotFoundException ex){
        log.error("User not found, " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "user_not_found",
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } 

    @ExceptionHandler (RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerRoleNotFound(RoleNotFoundException ex){
        log.error("Role not found in DB. Check Flyway migrations. " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "role_not_found",
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    } 

    @ExceptionHandler (UncorrectPasswordException.class)
    public ResponseEntity<ErrorResponse> handlerUncorrectPassword(UncorrectPasswordException ex){
        log.error("Uncorrect password " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "uncorrect_password",
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    } 

    @ExceptionHandler (UnvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handlerUnvalidToken(UnvalidTokenException ex){
        log.error("Unvalid token " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
            "unvalid_token",
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    } 

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerArgumentNotValid(MethodArgumentNotValidException ex){

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining("; "));

        log.error("Argument not valid, " + errorMessage);

        ErrorResponse response = new ErrorResponse(
            "argument_not_valid",
            errorMessage,
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    } 

}
