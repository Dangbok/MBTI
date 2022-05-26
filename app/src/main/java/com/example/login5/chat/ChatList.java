package com.example.login5.chat;

public class ChatList {
    //Firebase DB에 객체로 값을 읽어올 때 파라미터가 비어있는 생성자가 필요하기에 만듬

    private String mobile, name, message, time;

    public ChatList(String mobile, String name, String message, String time) {
        this.mobile = mobile;
        this.name = name;
        this.message = message;
        this.time = time;

    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
    //생성자 생성
}