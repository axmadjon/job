package uz.mold.job.internal;


import uz.mold.job.JobException;
import uz.mold.job.LongJob;
import uz.mold.job.Progress;

public class LongTask implements Runnable {

    public final int taskId;
    public final LongJob<Object> job;

    public LongTask(int taskId, LongJob<Object> job) {
        this.taskId = taskId;
        this.job = job;

        if (job == null) {
            throw JobException.NullPointer();
        }
    }

    @Override
    public void run() {
        try {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            Progress<Object> progress = new Progress<>(job.jobKey, taskId, Manager.getInstance());
            job.execute(progress);
            deliverStop(null);
        } catch (Throwable ex) {
            deliverStop(ex);
        }
    }

    private void deliverStop(final Throwable error) {
        final Manager manager = Manager.getInstance();
        manager.handler.post(new Runnable() {
            @Override
            public void run() {
                manager.onLongStop(job.jobKey, taskId, error);
            }
        });
    }

    @Override
    public String toString() {
        return "LongTask(" + taskId + ", " + job.jobKey + ", " + job.getClass().getName() + ")";
    }
}
