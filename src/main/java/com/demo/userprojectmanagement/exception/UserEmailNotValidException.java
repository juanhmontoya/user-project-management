package com.demo.userprojectmanagement.exception;

import static com.demo.userprojectmanagement.exception.ApiExceptionHandler.USER_EMAIL_NOT_VALID;

public class UserEmailNotValidException extends RuntimeException {

  public UserEmailNotValidException() {}

  @Override
  public String getMessage() {
    return USER_EMAIL_NOT_VALID;
  }
}
