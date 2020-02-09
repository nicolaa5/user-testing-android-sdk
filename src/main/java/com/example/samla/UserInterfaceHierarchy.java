package com.example.samla;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Stack;

public final class UserInterfaceHierarchy {
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
            final Pair<String, View> p = stack.pop();
            final View v = p.second;

            final boolean isLastOnLevel = stack.empty() || !p.first.equals(stack.peek().first);
            final String graphics = "" + p.first + (isLastOnLevel ? "└── " : "├── ");

            final String className = v.getClass().getSimpleName();
            final String line = graphics + className + " id=" + v.getId() + resolveIdToName(r, v);

            output.append(line).append("\n");

            if (v instanceof ViewGroup) {
                final ViewGroup vg = (ViewGroup) v;
                for (int i = vg.getChildCount() - 1; i >= 0; i--) {
                    stack.push(Pair.create(p.first + (isLastOnLevel ? "    " : "│   "), vg.getChildAt(i)));
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

}