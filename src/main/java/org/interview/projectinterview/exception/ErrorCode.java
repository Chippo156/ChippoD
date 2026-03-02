package org.interview.projectinterview.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
  ROLE_NOT_FOUND(400, "Role not found", HttpStatus.NOT_FOUND),
  USER_NOT_FOUND(400, "User not found", HttpStatus.NOT_FOUND),
  UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
  INVALID_TOKEN(400, "Invalid token", HttpStatus.BAD_REQUEST),
  EXPIRED_TOKEN(401, "Expired token", HttpStatus.UNAUTHORIZED);

  private final int code;
  private final String message;
  private final HttpStatusCode httpStatusCode;

  ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
    this.code = code;
    this.message = message;
    this.httpStatusCode = httpStatusCode;
  }
}
