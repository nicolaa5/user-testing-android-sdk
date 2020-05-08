package com.samla.sdk.analytics

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference


class ActivityManager (activity : Activity) {
    private val TAG = ActivityManager::class.java.simpleName
    var applicationActivity : Activity = activity;

    init {

        //var windowManager : WindowManager = activity.windowManager;
        //getViews(activity.window.decorView);

        activity.window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                // TODO: The system bars are visible. Make any desired
                // adjustments to your UI, such as showing the action bar or
                // other navigational controls.
            } else {
                // TODO: The system bars are NOT visible. Make any desired
                // adjustments to your UI, such as hiding the action bar or
                // other navigational controls.
            }
        }
    }


    var fragList: MutableList<WeakReference<Fragment>> =
        ArrayList<WeakReference<Fragment>>()

    @Override
    fun onAttachFragment(fragment: Fragment) {
        fragList.add(WeakReference(fragment))
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