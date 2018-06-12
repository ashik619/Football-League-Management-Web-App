package com.ashik619.testproject.models;

public class HttpRespMessage {
    private String message;

    public HttpRespMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
