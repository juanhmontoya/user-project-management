package com.demo.userprojectmanagement.exception;

import static com.demo.userprojectmanagement.exception.ApiExceptionHandler.USER_FIELD_NOT_VALID_MESSAGE;

public class UserFieldNotValidException extends RuntimeException {
  private String field;

  public UserFieldNotValidException(String field) {
    this.field = field;
  }

  @Override
  public String getMessage() {
    return String.format(USER_FIELD_NOT_VALID_MESSAGE, field);
  }
}
