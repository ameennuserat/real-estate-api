package com.graduation.realestateconsulting.exceptions;

import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
@Slf4j
public class GeneralHandling {


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage()));
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> methodArgumentNotValidHandle(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();

        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errors.toString())
                .build();

        log.error("MethodArgumentNotValidException {}",errors);

        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Failure")
                .error(exceptionResponse)
                .build();

        return new ResponseEntity<>(globalResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<?> runtimeException(RuntimeException ex) {
        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .build();

        log.error("RuntimeException",ex);

        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Failure")
                .error(exceptionResponse)
                .build();

        return new ResponseEntity<>(globalResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
//    public ResponseEntity<ExceptionDto> sqlIntegrityConstraintViolationHandle(SQLIntegrityConstraintViolationException ex) {
//
//        ExceptionDto exceptionResponse = ExceptionDto.builder()
//                .timestamp(new Date())
//                .statusCode(HttpStatus.BAD_REQUEST.value())
//                .message(ex.getLocalizedMessage())
//                .build();
//
//        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<?> noSuchElementHandle(NoSuchElementException ex) {

        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .build();

        log.error("NoSuchElementException",ex);

        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Failure")
                .error(exceptionResponse)
                .build();

        return new ResponseEntity<>(globalResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<?> illegalArgumentHandle(IllegalArgumentException ex) {

        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .build();

//        log.error("IllegalArgumentException",ex);

        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Failure")
                .error(exceptionResponse)
                .build();

        return new ResponseEntity<>(globalResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<?> accessDeniedException(AccessDeniedException ex) {

        ExceptionDto exceptionResponse = ExceptionDto.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .build();

        log.error("AccessDeniedException",ex);

        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Failure")
                .error(exceptionResponse)
                .build();

        return new ResponseEntity<>(globalResponse, HttpStatus.BAD_REQUEST);
    }
}
