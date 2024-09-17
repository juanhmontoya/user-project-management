package com.demo.userprojectmanagement.exception;

public class ProjectNotFoundException extends RuntimeException {
  public ProjectNotFoundException() {}

  @Override
  public String getMessage() {
    return ApiExceptionHandler.PROJECT_NOT_FOUND_MESSAGE;
  }
}
