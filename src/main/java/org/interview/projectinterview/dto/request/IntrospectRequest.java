package org.interview.projectinterview.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IntrospectRequest {
    private String token;
}
