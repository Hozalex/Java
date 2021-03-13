package javax.threading;

public abstract class CloseableTask implements Runnable, AutoCloseable {

    private boolean running;
    
    public boolean isRunning() {
        return running;
    }
    
    public final void execute() {
        run();
    }

    public final void executeAsync(int priority) {
        running = true;
        Thread thread = new Thread(this);
        thread.setPriority(priority);
        //thread.setDaemon(true);
        thread.start();
    }
    
    public final void executeAsync() {
        executeAsync(Thread.NORM_PRIORITY);
    }
    
    @Override
    public void close() throws Exception {
        running = false;
    }
    
}
