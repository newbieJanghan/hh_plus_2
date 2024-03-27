package org.example.course_register.api;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
    ErrorResponse errorResponse = new ErrorResponse("500", e.getMessage());
    return ResponseEntity.status(500).body(errorResponse);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
    return ResponseEntity.status(400).body(errorResponse);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
    ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
    return ResponseEntity.status(400).body(errorResponse);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
    ErrorResponse errorResponse = new ErrorResponse("404", e.getMessage());
    return ResponseEntity.status(404).body(errorResponse);
  }
}
