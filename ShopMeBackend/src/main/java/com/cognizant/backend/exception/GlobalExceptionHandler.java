package com.cognizant.backend.exception;

import com.cognizant.backend.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> responseNotFoundExceptionHandler(ResourceNotFoundException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message , false);
        return new ResponseEntity<>(apiResponse , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex){

        Map<String , String> resp = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((er) -> {
            String fieldName = ((FieldError) er).getField();
            String message = er.getDefaultMessage();
            resp.put(fieldName , message);
        });

        return new ResponseEntity<>(resp , HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex){

        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message , true);
        return new ResponseEntity<>(apiResponse , HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse> IOException(IOException ex){

        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message , false);
        return new ResponseEntity<>(apiResponse , HttpStatus.BAD_REQUEST);

    }
}
