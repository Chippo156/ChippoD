package org.interview.projectinterview.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    @NotBlank(message = "USERNAME_IS_REQUIRED")
    private String username;
    private String name;
    @Email(message = "INVALID_EMAIL")
    private String email;

    @Size(min = 6, message = "PASSWORD_MUST_BE_AT_LEAST_6_CHARACTERS")
    private String password;
}
