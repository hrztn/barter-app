package com.example.barterapp.model;

public class MessageModelClass {
    private String key;
    private String message;
    private String senderId;
    private String sendtime;

    public MessageModelClass() {
    }

    public MessageModelClass(String key, String message, String senderId, String sendtime) {
        this.key = key;
        this.message = message;
        this.senderId = senderId;
        this.sendtime = sendtime;
    }

    public MessageModelClass(String message, String senderId, String sendtime) {
        this.message = message;
        this.senderId = senderId;
        this.sendtime = sendtime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public MessageModelClass(String message, String senderId) {
        this.message = message;
        this.senderId = senderId;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

}
