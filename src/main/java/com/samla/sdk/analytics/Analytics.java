package com.samla.sdk.analytics;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Analytics {

    private static Context applicationContext;

    public Analytics(Context context) {
        applicationContext = context;
    }

   public static FirebaseAnalytics getFirebaseAnalytics () {
        return FirebaseAnalytics.getInstance(applicationContext);
   }

}
