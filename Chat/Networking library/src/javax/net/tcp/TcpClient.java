package javax.net.tcp;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TcpClient<E extends Serializable> implements Closeable {

    private Socket socket;
    
    private ObjectInputStream in;
    
    private ObjectOutputStream out;
    
    public TcpClient(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    public TcpClient(InetAddress address, int port) throws IOException {
        this(new Socket(address, port));
    }

    public final void send(E data) throws IOException {
        out.writeObject(data);
        out.flush();
    }
    
    public final E receive() throws IOException, ClassNotFoundException {
        return (E) in.readObject();
    }
    
    public SocketAddress getRemoteAddress() {
        return socket.getRemoteSocketAddress();
    }
    
    @Override
    public void close() throws IOException {
        socket.close();
        socket = null;
        in = null;
        out = null;
    }
    
}
