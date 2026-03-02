package org.interview.projectinterview.controllers;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.interview.projectinterview.dto.request.IntrospectRequest;
import org.interview.projectinterview.dto.request.LoginRequest;
import org.interview.projectinterview.dto.request.RefreshTokenRequest;
import org.interview.projectinterview.dto.response.AuthenticationResponse;
import org.interview.projectinterview.dto.response.IntrospectResponse;
import org.interview.projectinterview.dto.response.ResponseData;
import org.interview.projectinterview.services.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ResponseData<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        var response = authenticationService.login(request);
        return ResponseData.<AuthenticationResponse>builder()
                .data(response)
                .code(200)
                .message("Login success")
                .build();
    }

    @PostMapping("/introspect")
    ResponseData<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var response = authenticationService.introspect(request);
        return ResponseData.<IntrospectResponse>builder()
                .data(response)
                .code(200)
                .message("Introspection success")
                .build();
    }

    @PostMapping("/refresh")
    ResponseData<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        var response = authenticationService.generateRefreshToken(request);
        return ResponseData.<AuthenticationResponse>builder()
            .data(response)
            .code(200)
            .message("Refresh token success")
            .build();
    }
}
