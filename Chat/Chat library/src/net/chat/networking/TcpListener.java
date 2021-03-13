package net.chat.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.threading.RepeatableTask;

public class TcpListener extends RepeatableTask {
    
    private ServerSocket socket;
    
    private Set<ConnectionListener> listeners;
    
    public TcpListener(int port) throws IOException {
        listeners = new ConcurrentSkipListSet<>();
        socket = new ServerSocket(port);
    }
    
    public final void addConnectionListener(ConnectionListener listener) {
        listeners.add(listener);
    }
    
    public final void removeConnectionListener(ConnectionListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyIncomingConnection(Socket socket) {
        for (ConnectionListener listener : listeners) {
            listener.onIncomingConnection(socket);
        }
    }

    @Override
    protected void action() throws Exception {
        Socket client = socket.accept();
        notifyIncomingConnection(client);
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (socket != null) {
            socket.close();
            socket = null;
        }
        listeners.clear();
    }
    
}
