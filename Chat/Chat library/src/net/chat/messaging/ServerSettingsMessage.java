package net.chat.messaging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class ServerSettingsMessage implements Message {

    private final int port;

    private final String name;

    private final String ip;

    public ServerSettingsMessage(int port, String name) throws UnknownHostException {
        this.port = port;
        this.name = name;
        ip = InetAddress.getLocalHost().getHostAddress();
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;

    }

    public InetAddress getAddress() throws UnknownHostException {
        return InetAddress.getByName(ip);

    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ServerSettingsMessage loadFromConfig() throws IOException {
        try (FileInputStream stream = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            int port = Integer.parseInt(properties.getProperty("tcp.port"));
            String name = properties.getProperty("server.name");
            return new ServerSettingsMessage(port, name);
        }
    }

}
