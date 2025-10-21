package com.cuongph.be_code.exception;

import com.cuongph.be_code.response.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseData<Object>> handleAppException(AppException ex, HttpServletRequest request) {
        ResponseData<Object> response = new ResponseData<>()
                .error(ex.getStatus(), ex.getCode(), ex.getMessage())
                .path(request.getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        ResponseData<Object> response = new ResponseData<>()
                .error(500, "INTERNAL_ERROR", ex.getMessage())
                .path(request.getRequestURI());
        return ResponseEntity.status(500).body(response);
    }
}