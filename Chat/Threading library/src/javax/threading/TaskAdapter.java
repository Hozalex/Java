package javax.threading;

public class TaskAdapter implements TaskListener {

    @Override
    public void onClose(RepeatableTask task) {}

    @Override
    public void onInterrupted(RepeatableTask task) {}

}
