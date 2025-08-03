package com.strawhats.ecommercebackend.exception;

import com.strawhats.ecommercebackend.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(e.getMessage());
        apiResponse.setSuccess(Boolean.FALSE);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(e.getMessage());
        apiResponse.setSuccess(Boolean.FALSE);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
