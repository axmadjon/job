package uz.mold.job.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import uz.mold.job.Deferred;
import uz.mold.job.JobException;
import uz.mold.job.JobUtil;
import uz.mold.job.LongJob;
import uz.mold.job.LongJobListener;
import uz.mold.job.ShortJob;


public class Manager {

    public static final Handler handler = new Handler(Looper.getMainLooper());

    private final AtomicInteger sequence = new AtomicInteger();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private Context context;

    private final KeyContainer keyContainer = new KeyContainer();
    private final DeferredContainer deferredContainer = new DeferredContainer();
    private final RunningJobKeys runningJobKeys = new RunningJobKeys();

    public void execute(LongJob<Object> job) {
        checkMainLooper();
        runningJobKeys.checkDuplication(job.jobKey);
        int taskId = sequence.incrementAndGet();
        LongTask task = new LongTask(taskId, job);
        try {
            keyContainer.start(job.jobKey, taskId);
            executor.execute(task);
            jobServiceStart(taskId);
            runningJobKeys.add(job.jobKey, taskId);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    public void execute(ShortJob<Object> job, Deferred<Object> deferred, Object tag) {
        checkMainLooper();
        int taskId = sequence.incrementAndGet();
        ShortTask task = new ShortTask(taskId, job);
        try {
            deferredContainer.add(taskId, new DeferredWrapper(deferred, tag));
            executor.execute(task);
        } catch (Throwable ex) {
            deferredContainer.remove(taskId);
            throw new RuntimeException(ex);
        }
    }

    public void onLongProgress(String jobKey, int taskId, Object progress) {
        checkMainLooper();
        keyContainer.progress(jobKey, taskId, progress);
    }

    public void onLongStop(String jobKey, int taskId, Throwable error) {
        checkMainLooper();
        keyContainer.stop(jobKey, taskId, error);
        runningJobKeys.remove(jobKey);
        jobServiceStop(taskId);
    }

    public void onShortResult(int taskId, Object result) {
        checkMainLooper();
        deferredContainer.onResult(taskId, result);
    }

    public void stopListening(Object tag) {
        checkMainLooper();
        deferredContainer.removeByTag(tag);
        keyContainer.removeByTag(tag);
    }

    public boolean isRunning(String jobKey) {
        return runningJobKeys.isRunning(jobKey);
    }

    public void listenKey(String jobKey, LongJobListener<Object> jobListener, Object tag) {
        checkMainLooper();
        if (TextUtils.isEmpty(jobKey)) {
            throw JobException.NullPointer();
        }
        keyContainer.add(jobKey, jobListener, tag);
        KeyListeners listeners = keyContainer.getListeners(jobKey);

        if (runningJobKeys.isRunning(jobKey)) {
            keyContainer.progress(jobKey, runningJobKeys.getTaskId(jobKey), listeners.lastProgress);
        }
    }

    public void checkMainLooper() {
        JobUtil.checkMainLooper("Job manager methods must run in the main thread.");
    }

    private void jobServiceStart(int taskId) {
        if (context != null) {
            JobService.startJob(context, taskId);
        }
    }

    private void jobServiceStop(int taskId) {
        if (context != null) {
            JobService.stopJob(context, taskId);
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Manager() {
    }

    private static class LazyHolder {
        private static final Manager INSTANCE = new Manager();
    }

    public static Manager getInstance() {
        return LazyHolder.INSTANCE;
    }

}
