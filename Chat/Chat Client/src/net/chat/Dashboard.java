package net.chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.SocketAddress;
import javax.gui.AbstractFrame;
import javax.net.udp.UdpClient;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.chat.messaging.Message;
import net.chat.messaging.ServerSettingsMessage;
import net.chat.messaging.ServiceMessage;
import net.chat.networking.MessageListener;
import net.chat.networking.UdpReceiver;

public class Dashboard extends AbstractFrame implements MessageListener {

    public static void main(String[] args) throws Exception {
        Dashboard dashboard = new Dashboard();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dashboard.setVisible(true);
            }
        });
    }

    private class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            boolean enabled = list.getSelectedValue() != null;
            button.setEnabled(enabled);
        }

    }

    private final UdpReceiver receiver;

    private final JButton button;

    private final DefaultListModel<ServerSettingsMessage> model;

    private final JList<ServerSettingsMessage> list;

    public Dashboard() throws IOException {
        receiver = new UdpReceiver(9090);
        receiver.addMessageListener(this);
        button = new JButton("Connect");
        model = new DefaultListModel<>();
        list = new JList<>(model);
    }

    private void notifyPresence() {
        try (UdpClient<Message> client = new UdpClient()) {
            client.broadcast(ServiceMessage.ServerLookup, 8080);
        } catch (IOException e) {
            error("Error", e.getMessage());
        }
    }

    @Override
    protected void onInitComponents() {
        setTitle("Select chat server");
        setPreferredSize(new Dimension(400, 500));

        button.addActionListener(this);
        button.setEnabled(false);

        SelectionListener listener = new SelectionListener();
        ListSelectionModel selectionModel = list.getSelectionModel();
        selectionModel.addListSelectionListener(listener);
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane);
        add(button, BorderLayout.PAGE_END);

        receiver.executeAsync();
        notifyPresence();
    }

    @Override
    protected void onCommand(String command) {
        switch (command) {
            case "Connect":
                onConnect();
                break;

        }

    }

    private void onConnect() {
        ServerSettingsMessage settings = list.getSelectedValue();
        try {
            Chat chat = new Chat(settings);
        } catch (Exception e) {

            error("Error ", e.getMessage());

        }
    }

    @Override
    public void onMessage(SocketAddress address, Message message) {
        if (message instanceof ServerSettingsMessage) {
            ServerSettingsMessage settings = (ServerSettingsMessage) message;
            if (!model.contains(settings)) {
                model.addElement(settings);
            }
        }
    }

    @Override
    public void dispose() {
        try {
            receiver.close();
        } catch (Exception ignore) {
        }
        super.dispose();
    }

}
