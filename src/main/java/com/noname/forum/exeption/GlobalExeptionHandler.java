package com.noname.forum.exeption;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.noname.forum.controller.AuthController;
import com.noname.forum.dto.error.ErrorResponse;

@ControllerAdvice 
public class GlobalExeptionHandler {

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

    
}
