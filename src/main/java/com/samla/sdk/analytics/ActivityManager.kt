package com.samla.sdk.analytics

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference


class ActivityManager (applicationActivity : Activity) {
    private val TAG = ActivityManager::class.java.simpleName

    var activity : Activity = applicationActivity;
    var windowManager : WindowManager = activity.windowManager;

    init {
        getViews(activity.window.decorView);
    }


    var fragList: MutableList<WeakReference<Fragment>> =
        ArrayList<WeakReference<Fragment>>()

    @Override
    fun onAttachFragment(fragment: Fragment) {
        fragList.add(WeakReference(fragment))
    }

    fun application () {
        getViews(activity.window.decorView);
    }

    /**
     * Gets all fragments within the activity
     */
    fun getActiveFragments(): List<Fragment>? {
        val ret: ArrayList<Fragment> = ArrayList<Fragment>()
        for (ref in fragList) {
            val f: Fragment? = ref.get()
            if (f != null) {
                if (f.isVisible()) {
                    ret.add(f)
                }
            }
        }
        return ret
    }

    /**
     * Gets all views within the activity
     */
    private fun getViews(v: View) {
        val viewgroup = v as ViewGroup
        for (i in 0 until viewgroup.childCount) {
            val v1: View = viewgroup.getChildAt(i)
            if (v1 is ViewGroup) getViews(v1)
                Log.i(TAG, "AppName: $v1")
        }
    }
}