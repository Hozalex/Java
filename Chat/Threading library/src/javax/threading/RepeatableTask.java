package javax.threading;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public abstract class RepeatableTask extends CloseableTask {
    
    private final Set<TaskListener> listeners;
    
    public RepeatableTask() {
        listeners = new ConcurrentSkipListSet<>();
    }
    
    public final void addListener(TaskListener listener) {
        listeners.add(listener);
    }
    
    public final void removeListener(TaskListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyClose() {
        for (TaskListener listener : listeners) {
            listener.onClose(this);
        }
    }
    
    private void notifyInterrupted() {
        for (TaskListener listener : listeners) {
            listener.onInterrupted(this);
        }
    }
    
    protected abstract void action() throws Exception;
    
    private void dispose() {
        try {
            close();
        } catch (Exception ignore) {}
        listeners.clear();
    }

    @Override
    public void run() {
        try {
            while (isRunning()) action();
        } catch (Exception e) {
            if (isRunning()) notifyInterrupted();
        } finally {
            notifyClose();
            dispose();
        }
    }

    @Override
    public void close() throws Exception {
        if (isRunning()) super.close();
    }

}
