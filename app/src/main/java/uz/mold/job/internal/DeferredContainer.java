package uz.mold.job.internal;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DeferredContainer {

    @SuppressLint("UseSparseArrays")
    private final HashMap<Integer, DeferredWrapper> deferreds = new HashMap<>();

    public void add(int taskId, DeferredWrapper deferredWrapper) {
        deferreds.put(taskId, deferredWrapper);
    }

    public void remove(int taskId) {
        deferreds.remove(taskId);
    }

    public void onResult(int taskId, Object result) {
        DeferredWrapper d = deferreds.get(taskId);
        if (d == null) {
            return;
        }
        if (result instanceof Throwable) {
            d.deferred.reject((Throwable) result);
        } else {
            d.deferred.resolve(result);
        }
        deferreds.remove(taskId);
    }

    public void removeByTag(Object tag) {
        Iterator<Map.Entry<Integer, DeferredWrapper>> it = deferreds.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Integer, DeferredWrapper> v = it.next();
            if (v.getValue().tag == tag) {
                it.remove();
            }
        }
    }

}
