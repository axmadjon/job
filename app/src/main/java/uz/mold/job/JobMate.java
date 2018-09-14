package uz.mold.job;

import uz.mold.job.internal.Manager;


public class JobMate {

    private final Object TAG = new Object();

    @SuppressWarnings("unchecked")
    public <P> void execute(LongJob<P> job) {
        Manager.getInstance().execute((LongJob<Object>) job);
    }

    @SuppressWarnings("unchecked")
    public <R> Promise<R> execute(ShortJob<R> job) {
        Deferred<R> deferred = new Deferred<>();
        Manager.getInstance().execute((ShortJob<Object>) job, (Deferred<Object>) deferred, TAG);
        return deferred.promise();
    }

    public void stopListening() {
        Manager.getInstance().stopListening(TAG);
    }

    @SuppressWarnings("unchecked")
    public <P> void listenKey(String key, LongJobListener<P> jobListener) {
        Manager.getInstance().listenKey(key, (LongJobListener<Object>) jobListener, TAG);
    }


}
