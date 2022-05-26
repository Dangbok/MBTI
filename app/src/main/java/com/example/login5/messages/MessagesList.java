package com.example.login5.messages;

public class MessagesList {
    //Firebase DB에 객체로 값을 읽어올 때 파라미터가 비어있는 생성자가 필요하기에 만듬
    private String name, mobile, lastMessage, profilePic, chatKey;

    private int unseenMessages;

    public MessagesList(String name, String mobile, String lastMessage, String profilePic, int unseenMessages, String chatKey) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
        //변수 선언
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }

    //생성자 생성
}
