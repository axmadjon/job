package uz.mold.job.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uz.mold.job.LongJobListener;


public class KeyListeners {

    private final List<KeyListener> listeners = new ArrayList<>();
    public Object lastProgress;

    public void add(LongJobListener<Object> listener, Object tag) {
        KeyListener v = new KeyListener(listener, tag);
        listeners.add(v);
    }

    public void removeByTag(Object tag) {
        for (Iterator<KeyListener> iterator = listeners.iterator(); iterator.hasNext(); ) {
            KeyListener k = iterator.next();
            if (k.tag == tag) {
                iterator.remove();
            }
        }
    }

    public boolean isEmpty() {
        return listeners.isEmpty();
    }

    public void start(int taskId) {
        for (KeyListener listener : listeners) {
            listener.start(taskId);
        }
    }

    public void progress(int taskId, Object progress) {
        this.lastProgress = progress;
        for (KeyListener listener : listeners) {
            listener.progress(taskId, progress);
        }
    }

    public void stop(int taskId, Throwable error) {
        for (KeyListener listener : listeners) {
            listener.stop(taskId, error);
        }
    }

}
