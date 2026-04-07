package com.buren.playlog.exceptions;

import com.buren.playlog.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JWTAccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(JWTAccessDeniedException ex){
        return new ResponseEntity<>(new ErrorResponseDTO("Access Denied",LocalDate.now(),HttpStatus.FORBIDDEN),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException_.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException_ ex){
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage(), LocalDate.now(),HttpStatus.NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage(), LocalDate.now(),HttpStatus.CONFLICT),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WrongUsernameOrPasswordException.class)
    public ResponseEntity<ErrorResponseDTO> handleWrongUserNameOrPasswordException(WrongUsernameOrPasswordException ex){
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage(), LocalDate.now(),HttpStatus.UNAUTHORIZED),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ErrorResponseDTO> handlePasswordException(PasswordException ex){
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage(),LocalDate.now(),HttpStatus.UNAUTHORIZED),HttpStatus.UNAUTHORIZED);
    }

}
