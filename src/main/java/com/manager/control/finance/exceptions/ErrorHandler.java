package com.manager.control.finance.exceptions;

import com.manager.control.finance.utils.LocalDateUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseError> errorHandlerNotFound(){
        ResponseError error = new ResponseError(GlobalMessages.NOT_FOUND_ERROR );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
