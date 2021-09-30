package com.davistiba.wedemyserver.models;

public class MyCustomResponse {
    private String message;
    private Boolean success;

    public MyCustomResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public MyCustomResponse(String message) {
        this.message = message;
        this.success = true;
    }

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
