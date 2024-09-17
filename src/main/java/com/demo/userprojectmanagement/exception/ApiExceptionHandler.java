package com.demo.userprojectmanagement.exception;

import static com.demo.userprojectmanagement.util.Util.TIME_PATTERN;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
  public static final String USER_EMAIL_NOT_FOUND = "user email not found";
  public static final String USER_NAME_NOT_FOUND = "user with name=%s not found";
  public static final String USER_ID_NOT_FOUND = "user with id=%s not found";
  public static final String USER_EMAIL_NOT_VALID = "email format is not valid";
  public static final String USER_EMAIL_ALREADY_EXISTS = "given email already exists";
  public static final String USER_FIELD_NOT_VALID_MESSAGE = "field %s is not valid";
  public static final String USERS_NOT_CREATED_MESSAGE = "no users created";
  public static final String NO_PARAMS_PROVIDED_MESSAGE = "provide either email or name";

  public static final String PROJECT_NOT_FOUND_MESSAGE = "project not found";
  public static final String PROJECTS_NOT_LOADED_MESSAGE = "no projects loaded";
  public static final String PROJECT_NAME_NOT_VALID_MESSAGE = "not valid project name";
  public static final String PROJECT_ASSIGN_USERS_NOT_FOUND_MESSAGE =
      "users id/s [%s] were not found and not added to the project";
  public static final String PROJECT_UNASSIGN_USERS_NOT_FOUND_MESSAGE =
      "users id/s [%s] were not found and not unassign from the project";

  @ExceptionHandler(UserIdNotFoundException.class)
  protected ResponseEntity<?> handleApiRequestException(UserIdNotFoundException ex) {
    log.error(String.format("status %s. user id not found", HttpStatus.NOT_FOUND));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(UserNameNotFoundException.class)
  protected ResponseEntity<?> handleApiRequestException(UserNameNotFoundException ex) {
    log.error(String.format("status %s. user name not found", HttpStatus.NOT_FOUND));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(UserEmailNotFoundException.class)
  protected ResponseEntity<?> handleApiRequestException(UserEmailNotFoundException ex) {
    log.error(String.format("status %s. user email not found", HttpStatus.NOT_FOUND));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(UserEmailNotValidException.class)
  protected ResponseEntity<?> handleApiRequestException(UserEmailNotValidException ex) {
    log.error(String.format("status %s. user email not valid", HttpStatus.BAD_REQUEST));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(UserEmailExistsException.class)
  protected ResponseEntity<?> handleApiRequestException(UserEmailExistsException ex) {
    log.error(String.format("status %s. user email already exists", HttpStatus.CONFLICT));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(UserFieldNotValidException.class)
  protected ResponseEntity<?> handleApiRequestException(UserFieldNotValidException ex) {
    log.error(String.format("status %s. user email already exists", HttpStatus.BAD_REQUEST));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<?> notValidArgumentException(MethodArgumentTypeMismatchException ex) {
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(ProjectNotFoundException.class)
  protected ResponseEntity<?> handleApiRequestException(ProjectNotFoundException ex) {
    log.error(String.format("status %s. project not found", HttpStatus.NOT_FOUND));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(ProjectsNotLoadedException.class)
  protected ResponseEntity<?> handleApiRequestException(ProjectsNotLoadedException ex) {
    log.error(String.format("status %s. projects not loaded", HttpStatus.NOT_FOUND));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(ProjectNameNotValidException.class)
  protected ResponseEntity<?> handleApiRequestException(ProjectNameNotValidException ex) {
    log.error(String.format("status %s. project name not valid", HttpStatus.BAD_REQUEST));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(ProjectAssignException.class)
  protected ResponseEntity<?> handleApiRequestException(ProjectAssignException ex) {
    log.error(
        String.format(
            "status %s. User Id/s not found and can't be added to project", HttpStatus.NOT_FOUND));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }

  @ExceptionHandler(ProjectUnassignException.class)
  protected ResponseEntity<?> handleApiRequestException(ProjectUnassignException ex) {
    log.error(
        String.format(
            "status %s. User Id/s not found and can't be unassigned from project",
            HttpStatus.NOT_FOUND));
    ApiError error =
        new ApiError(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage());
    return error.toJSON();
  }
}
