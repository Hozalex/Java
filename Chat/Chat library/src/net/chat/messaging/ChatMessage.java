package net.chat.messaging;

import java.util.Date;

public abstract class ChatMessage implements Message {

    private final String user;
    
    private final Date time;
    
    public ChatMessage() {
        user = System.getProperty("user.name");
        time = new Date();
    }

    public String getUser() {
        return user;
    }

    public Date getTime() {
        return time;
    }
    
}
