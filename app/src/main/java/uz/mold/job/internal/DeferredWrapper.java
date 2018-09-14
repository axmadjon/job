package uz.mold.job.internal;

import uz.mold.job.Deferred;
import uz.mold.job.JobException;

public class DeferredWrapper {

    public final Deferred<Object> deferred;
    public final Object tag;

    public DeferredWrapper(Deferred<Object> deferred, Object tag) {
        this.deferred = deferred;
        this.tag = tag;

        if (deferred == null || tag == null) {
            throw JobException.NullPointer();
        }
    }
}
