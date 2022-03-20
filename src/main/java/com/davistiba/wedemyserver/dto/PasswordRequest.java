package com.davistiba.wedemyserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PasswordRequest {
    @NotBlank
    @JsonProperty("currentPass")
    private String currentPassword;

    @Size(min = 8, message = "Password is too short")
    @NotBlank
    @JsonProperty("newPass")
    private String newPassword;

    @NotBlank
    @JsonProperty("currentPass")
    private String confirmPassword;

    public PasswordRequest() {
    }

    /**
     * check if newPass == confirmPass
     *
     * @return boolean
     */
    public boolean isFirstEqualSecond() {
        return this.getConfirmPassword().equals(this.getNewPassword());
    }
}
