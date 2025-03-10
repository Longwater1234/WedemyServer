package com.davistiba.wedemyserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Response body of Hcaptcha API. Not in use.
 * https://docs.hcaptcha.com/
 */
@Getter
@ToString
public class CaptchaResponse {
    private Boolean success;

    private String hostname;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    @JsonProperty(value = "error-codes")
    private List<String> errorCodes;

    public CaptchaResponse() {
    }
}
