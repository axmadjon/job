package uz.mold.job.internal;


import uz.mold.job.JobException;
import uz.mold.job.LongJobListener;

public class KeyListener {

    public final LongJobListener<Object> listener;
    public final Object tag;

    private int runningTaskId;

    public KeyListener(LongJobListener<Object> listener, Object tag) {
        this.listener = listener;
        this.tag = tag;
        this.runningTaskId = -1;

        if (listener == null || tag == null) {
            throw JobException.NullPointer();
        }
    }

    void start(int taskId) {
        if (runningTaskId != taskId) {
            runningTaskId = taskId;
            listener.onStart();
        }
    }

    void stop(int taskId, Throwable error) {
        start(taskId);
        if (runningTaskId == taskId) {
            runningTaskId = -1;
            listener.onStop(error);
        }
    }

    void progress(int taskId, Object progress) {
        start(taskId);
        if (progress != null) {
            listener.onProgress(progress);
        }
    }

}
