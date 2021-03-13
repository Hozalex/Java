package net.chat.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import javax.net.tcp.TcpClient;
import net.chat.messaging.Message;

public class TcpConnection extends MessageReceiver {

    private TcpClient<Message> client;
    
    public TcpConnection(TcpClient client) {
        this.client = client;
    }
    
    public TcpConnection(Socket socket) throws IOException {
        this(new TcpClient<>(socket));
    }
    
    public TcpConnection(InetAddress address, int port) throws IOException {
        this(new TcpClient<>(address, port));
    }
    
    public void send(Message message) throws IOException {
        client.send(message);
    }
    
    @Override
    protected void action() throws Exception {
        Message message = client.receive();
        SocketAddress address = client.getRemoteAddress();
        notifyMessage(address, message);
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (client != null) {
            client.close();
            client = null;
        }
    }
    
    public final SocketAddress getRemoteAddress() {
        return client.getRemoteAddress();
    }

    public boolean equals(SocketAddress address) {
        return getRemoteAddress().equals(address);
    }

    @Override
    public String toString() {
        return client.getRemoteAddress().toString();
    }

}
