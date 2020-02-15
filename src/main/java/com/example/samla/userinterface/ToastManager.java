package com.example.samla.userinterface;

import android.app.Activity;
import android.widget.Toast;

public class ToastManager {
    public static void makeToast (final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
