package com.demo.userprojectmanagement.exception;

public class UserEmailNotFoundException extends RuntimeException {
  private String email;

  public UserEmailNotFoundException(String email) {
    this.email = email;
  }

  @Override
  public String getMessage() {
    return ApiExceptionHandler.USER_EMAIL_NOT_FOUND;
  }
}
