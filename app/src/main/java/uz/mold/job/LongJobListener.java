package uz.mold.job;

public interface LongJobListener<P> {

    void onStart();

    void onStop(Throwable error);

    void onProgress(P progress);

}
