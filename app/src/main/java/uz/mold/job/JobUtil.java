package uz.mold.job;

import android.os.Looper;

public class JobUtil {
    public static void checkMainLooper(String message) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException(message);
        }
    }
}
