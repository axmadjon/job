package uz.mold.job.internal;

import java.util.HashMap;

public class RunningJobKeys {

    private final HashMap<String, Integer> keys = new HashMap<>();

    public void checkDuplication(String jobKey) {
        if (keys.containsKey(jobKey)) {
            throw new RuntimeException(String.format("Duplicated job key [%s]", jobKey));
        }
    }

    public boolean isRunning(String jobKey) {
        return keys.containsKey(jobKey);
    }

    public int getTaskId(String jobKey) {
        return keys.get(jobKey);
    }

    public void add(String jobKey, int taskId) {
        checkDuplication(jobKey);
        keys.put(jobKey, taskId);
    }

    public void remove(String jobKey) {
        keys.remove(jobKey);
    }

}
