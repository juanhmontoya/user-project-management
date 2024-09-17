package com.demo.userprojectmanagement.exception;

public class UserNameNotFoundException extends RuntimeException {
  private String userName;

  public UserNameNotFoundException(String name) {
    this.userName = name;
  }

  @Override
  public String getMessage() {
    return String.format(ApiExceptionHandler.USER_NAME_NOT_FOUND, userName);
  }
}
