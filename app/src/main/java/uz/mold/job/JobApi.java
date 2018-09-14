package uz.mold.job;

import android.content.Context;

import uz.mold.job.internal.Manager;


public class JobApi {

    private static final JobMate JOB_HELPER = new JobMate();

    public static void runJobWithProgressInService(Context context) {
        Manager.getInstance().setContext(context);
    }

    public static boolean isRunning(String jobKey) {
        return Manager.getInstance().isRunning(jobKey);
    }

    public static <P> void execute(LongJob<P> job) {
        JOB_HELPER.execute(job);
    }

    public static <R> Promise<R> execute(ShortJob<R> job) {
        return JOB_HELPER.execute(job);
    }

    public static void stopListening() {
        JOB_HELPER.stopListening();
    }

    public static <P> void listenKey(String key, LongJobListener<P> jobListener) {
        JOB_HELPER.listenKey(key, jobListener);
    }
}
