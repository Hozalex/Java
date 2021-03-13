package javax.net.udp;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramSocket;

public abstract class UdpBase implements Closeable {
    
    protected DatagramSocket socket;
    
    @Override
    public void close() throws IOException {
        socket.close();
        socket = null;
    }

}
