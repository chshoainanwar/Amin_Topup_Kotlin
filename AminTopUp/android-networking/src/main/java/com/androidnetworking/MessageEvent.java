package com.androidnetworking;

public class MessageEvent {
    String message,title;

    public MessageEvent(String message, String title) {
        this.message = message;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}


