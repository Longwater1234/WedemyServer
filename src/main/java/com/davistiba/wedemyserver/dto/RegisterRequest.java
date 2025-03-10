package com.davistiba.wedemyserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @Size(max = 100)
    @NotBlank
    private String fullname;

    @Email
    @Pattern(regexp = "(^[0-9A-Za-z\\_%\\.\\-\\+]+@[\\w]+\\.[\\w]\\S+\\w)$", message = "Invalid email!")
    @NotBlank
    private String email;

    private String password;

    @NotBlank
    @Size(min = 8)
    private String confirmPass;
}
