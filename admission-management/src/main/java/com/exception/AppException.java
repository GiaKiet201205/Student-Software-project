package com.exception;

public class AppException extends RuntimeException{
  private String errorCode;

  public AppException(String message) {
    super(message);
  }

  public AppException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public AppException(String message, Throwable cause) {
    super(message, cause);
  }

  public String getErrorCode() {
    return errorCode;
  }
}
