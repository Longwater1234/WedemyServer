package com.davistiba.wedemyserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CaptchaResponse {
    private Boolean success;

    private String hostname;

    @JsonProperty(value = "error-codes")
    private List<String> errorCodes;

    public CaptchaResponse() {
    }
}
