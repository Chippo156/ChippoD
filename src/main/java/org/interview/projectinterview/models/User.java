package org.interview.projectinterview.models;

import jakarta.persistence.*;
import lombok.*;
import org.interview.projectinterview.enums.Gender;

import java.time.LocalDateTime;

@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {

    // User information
    private String name;
    private String email;
    private String avatar;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    @Enumerated
    private Gender gender;

    // Location information
    @Embedded
    private Location location;

    // OTP fields
    private String otp;
    private LocalDateTime otpExpiryTime;

    // URL
    private String cvUrl;
    private String facebookUrl;

    // Points
    private int points;

    // Authentication fields
    private String username;
    private String password;
    private String refreshToken;
    private LocalDateTime refreshTokenExpiryTime;
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;
}
