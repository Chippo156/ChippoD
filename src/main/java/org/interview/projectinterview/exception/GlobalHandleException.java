package org.interview.projectinterview.exception;

import org.interview.projectinterview.dto.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandleException {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ResponseData<?>> handlingRuntimeException(Exception e) {
        if (e instanceof AppException appException) {
            ResponseData<Object> responseData = new ResponseData<>();
            responseData.setCode(400);
            responseData.setMessage(e.getMessage());
            return ResponseEntity
                .status(appException.getErrorCode().getHttpStatusCode())
                .body(responseData);
        }
        return ResponseEntity
            .status(500)
            .body(ResponseData.builder()
                .code(500)
                .message("Internal Server Error")
                .build());
    }
}
