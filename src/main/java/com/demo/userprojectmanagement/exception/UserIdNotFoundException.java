package com.demo.userprojectmanagement.exception;

public class UserIdNotFoundException extends RuntimeException {
  private long userId;

  public UserIdNotFoundException(long id) {
    this.userId = id;
  }

  @Override
  public String getMessage() {
    return String.format(ApiExceptionHandler.USER_ID_NOT_FOUND, userId);
  }
}
