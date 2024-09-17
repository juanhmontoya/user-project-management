package com.demo.userprojectmanagement.exception;

public class ProjectsNotLoadedException extends RuntimeException {
  public ProjectsNotLoadedException() {}

  @Override
  public String getMessage() {
    return ApiExceptionHandler.PROJECTS_NOT_LOADED_MESSAGE;
  }
}
