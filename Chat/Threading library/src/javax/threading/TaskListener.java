package javax.threading;

public interface TaskListener {
    
    void onClose(RepeatableTask task);
    
    void onInterrupted(RepeatableTask task);
    
}
