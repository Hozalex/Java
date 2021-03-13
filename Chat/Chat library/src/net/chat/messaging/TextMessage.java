package net.chat.messaging;

public class TextMessage extends ChatMessage {

    private final String text;

    public TextMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
