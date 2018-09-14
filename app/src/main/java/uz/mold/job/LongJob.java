package uz.mold.job;

import android.text.TextUtils;


public abstract class LongJob<P> {

    public final String jobKey;

    protected LongJob(String jobKey) {
        this.jobKey = jobKey;
        if (TextUtils.isEmpty(jobKey)) {
            throw JobException.NullPointer();
        }
    }

    public abstract void execute(Progress<P> progress) throws Exception;


}
