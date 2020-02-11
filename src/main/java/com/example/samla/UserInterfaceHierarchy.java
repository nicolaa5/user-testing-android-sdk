package com.example.samla;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Stack;

public final class UserInterfaceHierarchy {
    private final static String TAG = UserInterfaceHierarchy.class.getSimpleName();
    /** Pint into log activity views hierarchy. */
    @NonNull
    public static String logViewHierarchy(@NonNull final Activity activity) {
        return logViewHierarchy(activity.findViewById(android.R.id.content));
    }

    /** Print into log view hierarchy. */
    @NonNull
    public static String logViewHierarchy(@NonNull final View root) {
        final StringBuilder output = new StringBuilder(8192).append("\n");
        final Resources r = root.getResources();
        final Stack<Pair<String, View>> stack = new Stack<>();
        stack.push(Pair.create("", root));

        while (!stack.empty()) {
            final Pair<String, View> pair = stack.pop();
            final View v = pair.second;

            final boolean isLastOnLevel = stack.empty() || !pair.first.equals(stack.peek().first);
            final String graphics = "" + pair.first + (isLastOnLevel ? "└── " : "├── ");

            final String className = v.getClass().getSimpleName();
            final String line = graphics + className + " id=" + v.getId() + resolveIdToName(r, v);

            output.append(line).append("\n");

            if (v instanceof ViewGroup) {
                final ViewGroup vg = (ViewGroup) v;
                for (int i = vg.getChildCount() - 1; i >= 0; i--) {
                    stack.push(Pair.create(pair.first + (isLastOnLevel ? "    " : "│   "), vg.getChildAt(i)));
                }
            }
        }

        return output.toString();
    }

    /** @see <a href="https://stackoverflow.com/questions/10137692/how-to-get-resource-name-from-resource-id">Lookup resource name</a> */
    @NonNull
    /* package */ private static String resolveIdToName(@Nullable final Resources r, @NonNull final View v) {
        if (null == r) return "";

        try {
            return " / " + r.getResourceEntryName(v.getId());
        } catch (Throwable ignored) {
            return "";
        }
    }

    public static void takeScreenShot(Activity activity) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // create bitmap screen capture
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File image = new File(activity.getFilesDir().toString() + "/" + now + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(image);

            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            // openScreenshot(activity, image);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private static void openScreenshot(Activity activity, File imageFile) {

        Uri uri = FileProvider.getUriForFile(activity,activity.getApplicationContext().getPackageName() + ".fileprovider", imageFile);

        Intent intent = new Intent()
                .setAction(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setDataAndType(uri, "image/*");

        activity.startActivity(intent);
    }

}