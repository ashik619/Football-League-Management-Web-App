package com.ashik619.testproject.models;

public class HttpRespMessage {
    private String message = null;
    private String data = null;

    public HttpRespMessage(String message){
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
