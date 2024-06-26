package com.manager.control.finance.exceptions;

import com.manager.control.finance.utils.GlobalMessages;
import com.manager.control.finance.dtos.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseMessage> errorHandlerNotFound(){
        ResponseMessage error = new ResponseMessage(GlobalMessages.NOT_FOUND_ERROR );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessage> errorHandlerIllegalArgument(IllegalArgumentException ex){
        ResponseMessage error = new ResponseMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
