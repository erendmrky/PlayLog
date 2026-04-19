package com.buren.playlog.exceptions;

import ch.qos.logback.classic.Logger;
import com.buren.playlog.dto.ErrorResponseDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = ((Logger) LoggerFactory.getLogger("EXCEPTIONS"));

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex){
        logger.error("Entity not found: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(new ErrorResponseDTO.ErrorCode(ErrorResponseDTO.ErrorCodeEnum.ENTITY_NOT_FOUND, ex.getMessage())
                ,LocalDate.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RawgException.class)
    public ResponseEntity<ErrorResponseDTO> handleRAWGException(RawgException ex){
        logger.error("RAWG API error: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(new ErrorResponseDTO.ErrorCode(ErrorResponseDTO.ErrorCodeEnum.RAWG,ex.getMessage())
                ,LocalDate.now()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityAlreadyExistsException(EntityExistsException ex){
        logger.error("Entity already exists: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(new ErrorResponseDTO.ErrorCode(ErrorResponseDTO.ErrorCodeEnum.ENTITY_EXISTS,ex.getMessage())
                ,LocalDate.now()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ErrorResponseDTO> handlePasswordException(PasswordException ex){
        logger.error("Password error: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(new ErrorResponseDTO.ErrorCode(ErrorResponseDTO.ErrorCodeEnum.PASSWORD_WRONG, ex.getMessage())
                ,LocalDate.now()),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponseDTO> handleSecurityException(SecurityException ex){
        logger.error("Security error: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(new ErrorResponseDTO.ErrorCode(ErrorResponseDTO.ErrorCodeEnum.BAD_REQUEST, ex.getMessage())
                ,LocalDate.now()),HttpStatus.BAD_REQUEST);
    }

}
