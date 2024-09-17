package com.demo.userprojectmanagement.exception;

public class ProjectAssignException extends RuntimeException {
  private final String ids;

  public ProjectAssignException(String ids) {
    this.ids = ids;
  }

  @Override
  public String getMessage() {
    return String.format(ApiExceptionHandler.PROJECT_ASSIGN_USERS_NOT_FOUND_MESSAGE, ids);
  }
}
