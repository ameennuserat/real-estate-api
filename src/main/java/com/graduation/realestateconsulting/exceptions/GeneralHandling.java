    package com.graduation.realestateconsulting.exceptions;

    import com.graduation.realestateconsulting.model.dto.response.ExceptionResponse;
    import jakarta.persistence.EntityNotFoundException;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.AccessDeniedException;
    import org.springframework.web.bind.MethodArgumentNotValidException;
    import org.springframework.web.bind.annotation.ControllerAdvice;
    import org.springframework.web.bind.annotation.ExceptionHandler;

    import java.util.Date;
    import java.util.List;
    import java.util.NoSuchElementException;

    @ControllerAdvice
    @Slf4j
    public class GeneralHandling {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ExceptionResponse> handleValidationExceptions(
                MethodArgumentNotValidException ex) {

            List<ExceptionDto> errors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(err -> ExceptionDto.builder()
                            .timestamp(new Date())
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(err.getField() + ": " + err.getDefaultMessage())
                            .build())
                    .toList();

            log.error("Validation failed: {}", errors);

            ExceptionResponse body = ExceptionResponse.builder()
                    .status("Failure")
                    .message("Validation failed for request")
                    .errors(errors)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(body);
        }

        @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
        public ResponseEntity<ExceptionResponse> handleNotFoundExceptions(
                RuntimeException ex) {

            ExceptionDto dto = ExceptionDto.builder()
                    .timestamp(new Date())
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message(ex.getLocalizedMessage())
                    .build();

            log.error("Resource not found", ex);

            ExceptionResponse body = ExceptionResponse.builder()
                    .status("Failure")
                    .message("Resource not found")
                    .errors(List.of(dto))
                    .build();

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(body);
        }

        @ExceptionHandler({IllegalArgumentException.class, AccessDeniedException.class})
        public ResponseEntity<ExceptionResponse> handleBadRequestExceptions(
                RuntimeException ex) {

            ExceptionDto dto = ExceptionDto.builder()
                    .timestamp(new Date())
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getLocalizedMessage())
                    .build();

            log.error("Bad request", ex);

            ExceptionResponse body = ExceptionResponse.builder()
                    .status("Failure")
                    .message("Bad request")
                    .errors(List.of(dto))
                    .build();

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(body);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ExceptionResponse> handleAllUncaught(
                Exception ex) {

            ExceptionDto dto = ExceptionDto.builder()
                    .timestamp(new Date())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(ex.getMessage())
                    .build();

            log.error("Internal server error", ex);

            ExceptionResponse body = ExceptionResponse.builder()
                    .status("Failure")
                    .message("Internal server error")
                    .errors(List.of(dto))
                    .build();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(body);
        }
    }
