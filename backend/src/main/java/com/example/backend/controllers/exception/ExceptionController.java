package com.example.backend.controllers.exception;

import com.example.backend.exceptions.ConflictException;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.exceptions.MyErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<MyErrorResponse> handleConflictException(ConflictException ex, HttpServletRequest request) {
        MyErrorResponse myErrorResponse = new MyErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(myErrorResponse);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<MyErrorResponse> handleItemNotFoundException(ItemNotFoundException ex, HttpServletRequest request) {
        MyErrorResponse myErrorResponse = new MyErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(myErrorResponse);
    }

}
