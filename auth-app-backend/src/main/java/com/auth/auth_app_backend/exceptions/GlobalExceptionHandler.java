package com.auth.auth_app_backend.exceptions;

import com.auth.auth_app_backend.dtos.ApiError;
import com.auth.auth_app_backend.dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler({
    UsernameNotFoundException.class,
    BadCredentialsException.class,
    CredentialsExpiredException.class,
    DisabledException.class
  })
  public ResponseEntity<ApiError> handleAuthException(Exception e, HttpServletRequest request) {
    logger.info("Exception: {}", e.getClass().getName());
    ApiError badRequest =
        ApiError.of(
            HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(), request.getRequestURI());
    return ResponseEntity.badRequest().body(badRequest);
  }

  // resource not found exception
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException exception) {
    ErrorResponse internaleServerError =
        new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internaleServerError);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException exception) {
    ErrorResponse internaleServerError =
        new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(internaleServerError);
  }
}
