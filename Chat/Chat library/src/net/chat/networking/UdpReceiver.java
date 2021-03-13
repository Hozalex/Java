package net.chat.networking;

import java.io.IOException;
import javax.net.udp.UdpListener;
import net.chat.messaging.Message;

public class UdpReceiver extends MessageReceiver {
    
    private final UdpListener<Message> listener;
    
    public UdpReceiver(int port) throws IOException {
        listener = new UdpListener<>(port);
    }

    @Override
    protected void action() throws Exception {
        Message message = listener.receive();
        notifyMessage(listener.getRemoteAddress(), message);
    }

    @Override
    public void close() throws Exception {
        super.close();
        listener.close();
    }
    
}
