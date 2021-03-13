package net.chat;

import java.awt.Dimension;
import java.net.SocketAddress;
import javax.gui.AbstractFrame;
import net.chat.messaging.Message;
import net.chat.messaging.ServerSettingsMessage;
import net.chat.networking.MessageListener;
import net.chat.networking.TcpConnection;

public class Chat extends AbstractFrame implements MessageListener {

    private final ServerSettingsMessage settings;

    private TcpConnection connection;

    public Chat(ServerSettingsMessage settings) throws Exception {
        this.settings = settings;
        connect();

    }

    private void connect() throws Exception {
        if (connection != null) {
            connection.close();
        }
        connection = new TcpConnection(settings.getAddress(), settings.getPort());
        connection.addMessageListener(this);
        connection.executeAsync();
    }

    @Override
    protected void onInitComponents() {
        setTitle(settings.getName());
        setSize(new Dimension(350, 500));
    }

    @Override
    public void onMessage(SocketAddress address, Message message) {

    }

    @Override
    public void dispose() {
        try {
            if (connection != null) {
                connection.close();
            }

        } catch (Exception ignore) {
        }

        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }

}
