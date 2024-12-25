package com.qlsv.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalException {

    private static final String RESPONSE_TIMESTAMP = "timestamp";
    private static final String RESPONSE_STATUS = "status";
    private static final String RESPONSE_PATH = "path";
    private static final String RESPONSE_DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
    private static final String REPLACE_URI = "uri=";
    private static final String REPLACE_EMPTY = "";
    private static final String RESPONSE_RESON = "";
    private static final String RESPONSE_MESSAGE = "System Error";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public Map<String, Object> handleException(Exception e, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        errors.put(RESPONSE_TIMESTAMP, new SimpleDateFormat(RESPONSE_DD_MM_YYYY_HH_MM_SS).format(new Date()));
        errors.put(RESPONSE_STATUS, HttpStatus.BAD_REQUEST.value());
        errors.put(RESPONSE_PATH, request.getDescription(false).replace(REPLACE_URI, REPLACE_EMPTY));
        errors.put(RESPONSE_RESON, RESPONSE_MESSAGE);

        return errors;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        errors.put(RESPONSE_TIMESTAMP, new SimpleDateFormat(RESPONSE_DD_MM_YYYY_HH_MM_SS).format(new Date()));
        errors.put(RESPONSE_STATUS, HttpStatus.BAD_REQUEST.value());
        errors.put(RESPONSE_PATH, request.getDescription(false).replace(REPLACE_URI, REPLACE_EMPTY));

        return errors;
    }
}
