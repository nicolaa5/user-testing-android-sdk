package com.samla.sdk.userflow

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.samla.sdk.ISamla
import com.samla.sdk.userflow.funnel.FunnelManager

object Analytics {

    lateinit var ISamla: ISamla

    fun setClientActivity(entry : ISamla) {
        ISamla = entry;
    }

    fun getFirebaseAnalytics (): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(ISamla.getActivity())
    }

}