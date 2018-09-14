package uz.mold.job;

import android.text.TextUtils;

public class JobException extends RuntimeException {

    public JobException() {
    }

    public JobException(String detailMessage) {
        super(detailMessage);
    }

    public JobException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public JobException(Throwable throwable) {
        super(throwable);
    }

    public static JobException Unsupported() {
        return new JobException("Unsupported");
    }

    public static JobException NullPointer() {
        return new JobException("NULL pointer");
    }

    public static void checkNull(Object... args) {
        for (Object a : args) {
            if (a == null) {
                throw NullPointer();
            }
        }
    }

    public static void require(boolean state, String msg) {
        if (!state) {
            throw new JobException(TextUtils.isEmpty(msg) ? "required" : msg);
        }
    }

    public static JobException Required() {
        return new JobException("Required");
    }

}
