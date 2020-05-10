package com.samla.sdk.userflow.funnel

import android.util.Log
import com.samla.sdk.ISamla
import com.samla.sdk.userflow.Analytics.Companion.firebaseAnalytics
import com.samla.sdk.userinterface.ToastManager.makeToast
import java.util.*

object FunnelManager {
    private val TAG = FunnelManager::class.java.simpleName

    lateinit var ISamla: ISamla
    private val wayPoints = ArrayList<WayPoint>()

    fun setClientActivity(entry : ISamla) {
        ISamla = entry;
    }


    fun reachedWayPoint(wayPoint: Int) {
        wayPoints.add(WayPoint(wayPoint))
        Log.i(TAG, "Waypoints: $wayPoints")
        makeToast(ISamla.getActivity(), "Reached Waypoint: $wayPoint")
        firebaseAnalytics.setUserProperty(
            "milestone",
            wayPoint.toString()
        )
    }
}