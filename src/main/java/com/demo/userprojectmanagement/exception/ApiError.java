package com.demo.userprojectmanagement.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiError {
  private final String timestamp;
  private final Integer status;
  private final String error;
  private final String message;

  public ApiError(String timestamp, Integer status, String error, String message) {
    super();
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.message = message;
  }

  public ResponseEntity<?> toJSON() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            Map.of(
                "timestamp", this.timestamp,
                "status", this.status,
                "error", this.error,
                "message", this.message));
  }
}
