package uz.mold.job.internal;


import uz.mold.job.ShortJob;

public class ShortTask implements Runnable {

    public final int taskId;
    public final ShortJob<Object> job;

    public ShortTask(int taskId, ShortJob<Object> job) {
        this.taskId = taskId;
        this.job = job;
    }

    @Override
    public void run() {
        try {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            Object result = job.execute();
            deliverResult(result);
        } catch (Throwable ex) {
            deliverResult(ex);
        }
    }

    private void deliverResult(final Object result) {
        final Manager manager = Manager.getInstance();
        manager.handler.post(new Runnable() {
            @Override
            public void run() {
                manager.onShortResult(taskId, result);
            }
        });
    }

    @Override
    public String toString() {
        return "ShortTask(" + taskId + ", " + job.getClass().getName() + ")";
    }

}
