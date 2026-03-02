package org.interview.projectinterview.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class AuthenticationResponse {
    private String token;
    private String role;
    private boolean authenticated;
}
