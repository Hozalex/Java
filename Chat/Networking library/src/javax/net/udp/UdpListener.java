package javax.net.udp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpListener<E> extends UdpBase {
    
    private byte[] data;
    
    private DatagramPacket packet;
    
    public UdpListener(SocketAddress address) throws SocketException, IOException {
        socket = new DatagramSocket(address);
        data = new byte[4096];
        packet = new DatagramPacket(data, data.length);
    }
    
    public UdpListener(InetAddress address, int port) throws SocketException, IOException {
        this(new InetSocketAddress(address, port));
    }
    
    public UdpListener(String address, int port) throws SocketException, UnknownHostException, IOException {
        this(InetAddress.getByName(address), port);
    }

    public UdpListener(int port) throws SocketException, IOException {
        this(new InetSocketAddress(port));
    }
    
    public SocketAddress getRemoteAddress() {
        return packet.getSocketAddress();
    }
    
    private ObjectInputStream stream() throws IOException {
        return new ObjectInputStream(new ByteArrayInputStream(data));
    }
        
    public E receive() throws IOException, ClassNotFoundException {
        socket.receive(packet);
        try (ObjectInputStream stream = stream()) {
            return (E) stream.readObject();
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        packet = null;
        data = null;
    }
    
}
