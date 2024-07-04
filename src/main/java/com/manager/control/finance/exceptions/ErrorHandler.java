package com.manager.control.finance.exceptions;

import com.manager.control.finance.utils.GlobalMessages;
import com.manager.control.finance.dtos.ResponseMessageDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseMessageDTO> errorHandlerNotFound(){
        ResponseMessageDTO error = new ResponseMessageDTO(GlobalMessages.NOT_FOUND_ERROR );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessageDTO> errorHandlerIllegalArgument(IllegalArgumentException ex){
        ResponseMessageDTO error = new ResponseMessageDTO(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseMessageDTO> errorHandlerDataIntegrityViolation(){
        ResponseMessageDTO error = new ResponseMessageDTO(GlobalMessages.DATA_INTEGRITY_VIOLATION);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
