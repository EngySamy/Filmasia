package com.example.engy.filmasia.firabase;

/**
 * Created by Engy on 2/27/2018.
 */

public class ChatMessage {
    private String sender;
    private String textMsg;
    private String photoUrl;

    public ChatMessage(){}

    public ChatMessage(String send,String msg,String url){
        sender=send;
        textMsg=msg;
        photoUrl=url;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
