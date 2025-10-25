package com.example.backend.controllers.exception;

import com.example.backend.exceptions.ConflictException;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.exceptions.MyErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MyErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        MyErrorResponse myErrorResponse = new MyErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation error. Check 'errors' field for details.", request.getRequestURI());
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            myErrorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(myErrorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<MyErrorResponse> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        MyErrorResponse myErrorResponse = new MyErrorResponse(ex.getStatusCode().value(), ex.getReason(), request.getRequestURI());
        return ResponseEntity.status(ex.getStatusCode()).body(myErrorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        MyErrorResponse myErrorResponse = new MyErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(myErrorResponse);
    }
}