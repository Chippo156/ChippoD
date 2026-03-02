package org.interview.projectinterview.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IntrospectResponse {
    boolean valid;
    String scope;
    UserResponse user;
}
