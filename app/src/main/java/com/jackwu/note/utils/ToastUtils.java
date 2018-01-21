package com.jackwu.note.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


public class ToastUtils {
    private ToastUtils() {
    }

    private static Toast toast;
    private static Handler handler = new Handler(Looper.getMainLooper());


    public static void showShortToastSafe(final CharSequence text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                showToast(text, Toast.LENGTH_SHORT);
            }
        });
    }

    public static void showLongToastSafe(final CharSequence text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                showToast(text, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 显示
     *
     * @param text     文本
     * @param duration 显示时长
     */
    private static void showToast(CharSequence text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(AppUtils.getContext(), text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }




    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}
