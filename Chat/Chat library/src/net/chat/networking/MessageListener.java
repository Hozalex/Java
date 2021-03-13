package net.chat.networking;

import java.net.SocketAddress;
import net.chat.messaging.Message;

public interface MessageListener {
    
    void onMessage(SocketAddress address, Message message);
    
}
