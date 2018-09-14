package uz.mold.job;


import uz.mold.job.internal.Manager;

public class Progress<P> {

    private final String jobKey;
    private final int taskId;
    private final Manager manager;

    public Progress(String jobKey, int taskId, Manager manager) {
        this.jobKey = jobKey;
        this.taskId = taskId;
        this.manager = manager;
    }

    public final void notify(final P progress) {
        Manager.handler.post(new Runnable() {
            @Override
            public void run() {
                manager.onLongProgress(jobKey, taskId, progress);
            }
        });
    }
}
