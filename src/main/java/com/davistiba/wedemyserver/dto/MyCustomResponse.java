package com.davistiba.wedemyserver.dto;

import lombok.Getter;

/**
 * Generic response with message
 */
@Getter
public class MyCustomResponse {
    private final String message;
    private final Boolean success;

    public MyCustomResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public MyCustomResponse(String message) {
        this.message = message;
        this.success = true;
    }

    public static MyCustomResponse fail(String message) {
        return new MyCustomResponse(message, false);
    }

}
