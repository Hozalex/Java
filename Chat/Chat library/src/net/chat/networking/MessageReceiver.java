package net.chat.networking;

import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;
import javax.threading.RepeatableTask;
import net.chat.messaging.Message;

public abstract class MessageReceiver extends RepeatableTask {

    private final Set<MessageListener> listeners;

    public MessageReceiver() {
        listeners = new HashSet<>();
    }

    public final void addMessageListener(MessageListener listener) {
        listeners.add(listener);
    }

    public final void removeMessageListener(MessageListener listener) {
        listeners.remove(listener);
    }

    protected final void notifyMessage(SocketAddress address, Message message) {
        for (MessageListener listener : listeners) {
            listener.onMessage(address, message);
        }
    }

    @Override
    public void close() throws Exception {
        super.close();
        listeners.clear();
    }

}
