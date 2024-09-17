package com.demo.userprojectmanagement.exception;

public class ProjectNameNotValidException extends RuntimeException {
  public ProjectNameNotValidException() {}

  @Override
  public String getMessage() {
    return ApiExceptionHandler.PROJECT_NAME_NOT_VALID_MESSAGE;
  }
}
