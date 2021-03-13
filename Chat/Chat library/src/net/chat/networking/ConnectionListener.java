package net.chat.networking;

import java.net.Socket;

public interface ConnectionListener {
    
    void onIncomingConnection(Socket socket);
    
}
