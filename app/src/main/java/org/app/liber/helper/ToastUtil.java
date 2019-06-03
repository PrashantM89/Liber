package org.app.liber.helper;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
    public enum Duration {
        SHORT, LONG
    }

    private ToastUtil() {
        throw new AssertionError();
    }

    public static Toast showToast(Context context, int stringResId) {
        return showToast(context, stringResId, Duration.SHORT);
    }

    public static Toast showToast(Context context, int stringResId, Duration duration) {
        return showToast(context, context.getString(stringResId), duration);
    }

    public static Toast showToast(Context context, String text) {
        return showToast(context, text, Duration.SHORT);
    }

    public static Toast showToast(Context context, String text, Duration duration) {
        return showToast(context, text, duration, Gravity.CENTER);
    }

    public static Toast showToast(Context context, String text, Duration duration, int gravity) {
        Toast toast = Toast.makeText(context, text,
                (duration == Duration.SHORT ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG));
        toast.setGravity(gravity, 0, 0);
        toast.show();
        return toast;
    }
}