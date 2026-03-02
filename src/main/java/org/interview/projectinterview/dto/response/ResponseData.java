package org.interview.projectinterview.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> {
    private String message;
    private int code;
    private T data;
}
