package javax.net.udp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClient<E extends Serializable> extends UdpBase {
    
    private SocketAddress address;
    
    public UdpClient() throws SocketException, IOException {
        socket = new DatagramSocket();
    }

    public UdpClient(SocketAddress address) throws SocketException, IOException {
        this();
        this.address = address;
    }
    
    public UdpClient(InetAddress address, int port) throws SocketException, IOException {
        this(new InetSocketAddress(address, port));
    }
    
    public UdpClient(String address, int port) throws UnknownHostException, SocketException, IOException {
        this(InetAddress.getByName(address), port);
    }
    
    public void send(E message) throws IOException {
        if (address == null) {
            throw new IllegalStateException("No socket address supplied!");
        }
        send(message, address);
    }
    
    public void broadcast(E message, int port) throws IOException {
        socket.setBroadcast(true);
        send(message, new InetSocketAddress("255.255.255.255", port));
        socket.setBroadcast(false);
    }
    
    public void send(E message, SocketAddress address) throws IOException {
        try (   ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                ObjectOutputStream stream = new ObjectOutputStream(buffer)) {
            stream.writeObject(message);
            byte[] data = buffer.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, address);
            socket.send(packet);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        address = null;
    }

}
