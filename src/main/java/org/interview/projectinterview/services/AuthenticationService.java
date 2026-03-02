package org.interview.projectinterview.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.interview.projectinterview.dto.request.IntrospectRequest;
import org.interview.projectinterview.dto.request.LoginRequest;
import org.interview.projectinterview.dto.request.RefreshTokenRequest;
import org.interview.projectinterview.dto.response.AuthenticationResponse;
import org.interview.projectinterview.dto.response.IntrospectResponse;
import org.interview.projectinterview.dto.response.UserResponse;
import org.interview.projectinterview.exception.AppException;
import org.interview.projectinterview.exception.ErrorCode;
import org.interview.projectinterview.mapper.UserMapper;
import org.interview.projectinterview.models.InvalidDateToken;
import org.interview.projectinterview.models.User;
import org.interview.projectinterview.repositories.InvalidDateTokenRepository;
import org.interview.projectinterview.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    UserMapper userMapper;
    InvalidDateTokenRepository invalidDateTokenRepository;
    @Value("${jwt.secretKey}")
    @NonFinal
    private String secretKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private Long validDuration;

    @NonFinal
    @Value("${jwt.refresh-duration}")
    private Long refreshableDuration;

    public AuthenticationResponse login(LoginRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!Boolean.TRUE.equals(user.isActive())) {
            throw new RuntimeException("User is not active");
        }

        String token = generateToken(user);

        String role = user.getRole().getRoleName();

        return AuthenticationResponse.builder()
                .token(token)
                .role(role)
                .authenticated(Boolean.TRUE)
                .build();
    }

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("d-learning")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user.getRole().getRoleName()).build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponse generateRefreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signedJWT = verification(request.getToken(), true);
        var jid = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidDateToken invalidDateToken = InvalidDateToken.builder()
            .id(jid)
            .expiryTime(expiryTime)
            .build();

        invalidDateTokenRepository.save(invalidDateToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return AuthenticationResponse.builder()
            .authenticated(Boolean.TRUE)
            .token(generateToken(user))
            .role(user.getRole().getRoleName())
            .build();

    }

    public SignedJWT verification(String token, boolean isRefresh) throws JOSEException, ParseException {
        if (token == null || token.trim().isEmpty()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh) ?
            new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(refreshableDuration, ChronoUnit.HOURS).toEpochMilli()) :
            signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expiryTime.before(new Date())) {
            throw  new AppException(ErrorCode.EXPIRED_TOKEN);
        }
        var verified = signedJWT.verify(verifier);
        if (!verified) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        return signedJWT;
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        boolean isValid = true;
        String scope = "";
        String subject = "";
        try {
            SignedJWT signedJWT = verification(request.getToken(), false);
            scope = (String) signedJWT.getJWTClaimsSet().getClaim("scope");
            subject = signedJWT.getJWTClaimsSet().getSubject();

        } catch (AppException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        log.info("Introspect token: {}, valid: {}, scope: {}, subject: {}", request.getToken(), isValid, scope, subject);
        UserResponse userResponse = userMapper.toUserResponse(userRepository.findByUsername(subject).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        return IntrospectResponse.builder()
            .valid(isValid)
            .scope(scope)
            .user(userResponse)
            .build();
    }
}
