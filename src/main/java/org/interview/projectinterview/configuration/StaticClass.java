package org.interview.projectinterview.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.interview.projectinterview.dto.response.ResponseData;
import org.interview.projectinterview.exception.ErrorCode;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class StaticClass {
    static void errorCodeAuthenticated(HttpServletResponse response) throws IOException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        response.setStatus(errorCode.getCode());
        response.setContentType("application/json");

        ResponseData<Object> apiResponse = ResponseData.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
