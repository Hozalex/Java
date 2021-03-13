package net.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.net.udp.UdpClient;
import javax.threading.RepeatableTask;
import javax.threading.TaskListener;
import net.chat.messaging.ChatMessage;
import net.chat.messaging.Message;
import net.chat.messaging.ServerSettingsMessage;
import net.chat.messaging.ServiceMessage;
import net.chat.networking.ConnectionListener;
import net.chat.networking.MessageListener;
import net.chat.networking.TcpConnection;
import net.chat.networking.TcpListener;
import net.chat.networking.UdpReceiver;

public class Server implements MessageListener, ConnectionListener, TaskListener, AutoCloseable {

    public static void main(String[] args) throws Exception {
        try (Server server = new Server()) {
            System.out.println("Server started! To exit press ENTER >>");
            System.in.read();
        }
    }

    private final List<TcpConnection> connections;

    private final TcpListener listener;

    private final UdpReceiver receiver;

    private final UdpClient client;

    private final ServerSettingsMessage settings;

    public Server() throws IOException {
        settings = ServerSettingsMessage.loadFromConfig();

        receiver = new UdpReceiver(8080);
        receiver.addMessageListener(this);
        receiver.executeAsync();

        client = new UdpClient<>();
        client.broadcast(settings, 9090);

        connections = new ArrayList<>();

        listener = new TcpListener(10001);
        listener.addConnectionListener(this);
        listener.executeAsync();
    }

    @Override
    public void onMessage(SocketAddress address, Message message) {
        if (message instanceof ServiceMessage) {
            onServiceMessage((InetSocketAddress) address, (ServiceMessage) message);
        } else if (message instanceof ChatMessage) {
            onChatMessage(address, (ChatMessage) message);
        }
    }

    private void onChatMessage(SocketAddress sender, ChatMessage message) {
        try {
            for (TcpConnection connection : connections) {
                if (connection.equals(sender)) {
                    continue;
                }
                connection.send(message);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void onServiceMessage(InetSocketAddress address, ServiceMessage message) {
        try {
            switch (message) {
                case ServerLookup:
                    address = new InetSocketAddress(address.getAddress(), 9090);
                    client.send(settings, address);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onIncomingConnection(Socket socket) {
        try {
            TcpConnection connection = new TcpConnection(socket);
            connection.addMessageListener(this);
            connection.addListener(this);
            connections.add(connection);
            connection.executeAsync();
            System.out.println("Incoming connection from " + connection);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void onClose(RepeatableTask task) {
        if (task instanceof TcpConnection) {
            TcpConnection connection = (TcpConnection) task;
            System.out.println("Connection closed " + connection);
            connections.remove(connection);
        }
    }

    @Override
    public void onInterrupted(RepeatableTask task) {
        if (task instanceof TcpConnection) {
            TcpConnection connection = (TcpConnection) task;
            System.err.println("Connection to " + connection + " has been lost!");
        }
    }

    @Override
    public void close() throws Exception {
        receiver.close();
        client.close();
        listener.close();
        for (TcpConnection connection : connections) {
            connection.close();
        }
    }

}
