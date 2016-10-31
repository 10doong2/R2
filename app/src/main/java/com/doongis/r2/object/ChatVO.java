package com.doongis.r2.object;


public class ChatVO {

    public String message;
    public String author;

    public ChatVO() {}

    public ChatVO(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public String getAuthor() {return author;}

    public String getMessage() {return message;}

}
