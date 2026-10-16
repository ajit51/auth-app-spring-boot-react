package com.auth.auth_app_backend.exceptions;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException() {
    super("Resource not found!!");
  }

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
