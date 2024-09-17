package com.demo.userprojectmanagement.exception;

public class ProjectUnassignException extends RuntimeException {
  private final String ids;

  public ProjectUnassignException(String ids) {
    this.ids = ids;
  }

  @Override
  public String getMessage() {
    return String.format(ApiExceptionHandler.PROJECT_UNASSIGN_USERS_NOT_FOUND_MESSAGE, ids);
  }
}
