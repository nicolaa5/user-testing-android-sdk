package com.example.samla.funnel;

import android.app.Activity;
import android.util.Log;
import com.example.samla.userinterface.ToastManager;

import java.util.ArrayList;

public class FunnelManager {
    private final static String TAG = FunnelManager.class.getSimpleName();

    private Activity activity;
    private ArrayList<WayPoint> wayPoints = new ArrayList<>();

    public FunnelManager (Activity activity) {
        this.activity = activity;
    }

    public void reachedWayPoint (int wayPoint) {
        wayPoints.add(new WayPoint(wayPoint));
        Log.i(TAG, "Waypoints: " + wayPoints.toString());
        ToastManager.makeToast(activity, "Reached Waypoint: " + wayPoint);
    }
}
