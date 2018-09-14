package uz.mold.job.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uz.mold.job.LongJobListener;


public class KeyContainer {

    private final HashMap<String, KeyListeners> listeners = new HashMap<>();

    public KeyListeners getListeners(String key) {
        return listeners.get(key);
    }

    public void add(String key, LongJobListener<Object> listener, Object tag) {
        KeyListeners keyListeners = listeners.get(key);
        if (keyListeners == null) {
            keyListeners = new KeyListeners();
            listeners.put(key, keyListeners);
        }
        keyListeners.add(listener, tag);
    }

    public void removeByTag(Object tag) {
        Iterator<Map.Entry<String, KeyListeners>> it = listeners.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, KeyListeners> entry = it.next();
            entry.getValue().removeByTag(tag);
            if (entry.getValue().isEmpty()) {
                it.remove();
            }
        }
    }

    public void start(String jobKey, int taskId) {
        KeyListeners keyListeners = listeners.get(jobKey);
        if (keyListeners != null) {
            keyListeners.start(taskId);
        }
    }

    public void progress(String jobKey, int taskId, Object progress) {
        KeyListeners keyListeners = listeners.get(jobKey);
        if (keyListeners != null) {
            keyListeners.progress(taskId, progress);
        }
    }

    public void stop(String jobKey, int taskId, Throwable error) {
        KeyListeners keyListeners = listeners.get(jobKey);
        if (keyListeners != null) {
            keyListeners.stop(taskId, error);
        }
    }

}
