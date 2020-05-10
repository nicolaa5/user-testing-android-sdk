package com.samla.sdk.userflow

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

class Analytics(private val applicationContext: Context) {

    init{
        Analytics.context = applicationContext;
    }

    companion object {
        private lateinit var context: Context

        @JvmStatic
        val firebaseAnalytics: FirebaseAnalytics
            get() {
                return FirebaseAnalytics.getInstance(context)
            }
    }

}