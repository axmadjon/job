package uz.mold.job.internal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.SparseBooleanArray;

public class JobService extends Service {

    private static final String TASK_ID = "TASK_ID";
    private static final String ACTION_START = "ACTION_START";

    private static void performJobService(Context context, int taskID, boolean actionStart) {
        Intent i = new Intent(context, JobService.class);
        i.putExtra(TASK_ID, taskID);
        i.putExtra(ACTION_START, actionStart);
        context.startService(i);
    }

    public static void startJob(Context context, int taskId) {
        performJobService(context, taskId, true);
    }

    public static void stopJob(Context context, int taskId) {
        performJobService(context, taskId, false);
    }

    private SparseBooleanArray taskIds;

    @Override
    public void onCreate() {
        super.onCreate();
        taskIds = new SparseBooleanArray();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        int taskId = extras.getInt(TASK_ID);
        boolean actionStart = extras.getBoolean(ACTION_START);
        if (actionStart) {
            taskIds.put(taskId, true);
        } else {
            taskIds.delete(taskId);
        }
        if (taskIds.size() <= 0) {
            stopSelf(startId);
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
