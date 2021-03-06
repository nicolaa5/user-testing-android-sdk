package com.samla.sdk

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseAppLifecycleListener
import com.google.firebase.FirebaseOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.samla.sdk.storage.DataStorage
import com.samla.sdk.userflow.Analytics
import com.samla.sdk.userflow.funnel.FunnelManager
import com.samla.sdk.userinterface.ActivityManager
import com.samla.sdk.userinterface.UIHierarchy


class Samla constructor(context : Context) : LifecycleObserver, SamlaBuilder, FragmentManager.OnBackStackChangedListener, ISamla {
    private val TAG = Samla::class.java.simpleName

    val mContext : Context
    val mActivity: Activity
    lateinit var mLifeCycle: Lifecycle;

    lateinit var firebaseAnalytics: FirebaseAnalytics;
    lateinit var firebaseAppLifecycleListener: FirebaseAppLifecycleListener;
    lateinit var firebaseApp: FirebaseApp;
    lateinit var firebaseOptions: FirebaseOptions;


    init {
        mContext  = context
        mActivity = context as Activity

        FunnelManager.setClientActivity(this);
        ActivityManager.setClientActivity(this);
        DataStorage.setClientActivity(this);
        Analytics.setClientActivity(this);
    }

    override fun getActivity(): Activity {
        return mActivity
    }

    override fun withLifeCycle(lifeCycle: Lifecycle): SamlaBuilder {
        mLifeCycle = lifeCycle
        mLifeCycle.addObserver(this)
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(lifecycleOwner: LifecycleOwner?) {
        Log.i(TAG, "onCreated")

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.i(TAG, "onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.i(TAG, "onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Log.i(TAG, "onStop")
        val hierarchy = UIHierarchy.logViewHierarchy(mActivity)
        Log.i(TAG, "UI Hierarchy: $hierarchy")
        UIHierarchy.getScreenshot(mActivity)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.i(TAG, "onDestroy")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(source: LifecycleOwner?, event: Lifecycle.Event?) {
    }

    fun menuListener () {
        UIHierarchy.setMenuListener(
            mActivity.findViewById(
                R.id.content
            )
        )
    }

    fun test() {
        val manager: FragmentManager =
            object : FragmentManager() {
                override fun findFragmentByTag(tag: String?): Fragment? {
                    return super.findFragmentByTag(tag)
                }

                override fun addOnBackStackChangedListener(listener: OnBackStackChangedListener) {
                    super.addOnBackStackChangedListener(listener)
                }

                override fun removeOnBackStackChangedListener(listener: OnBackStackChangedListener) {
                    super.removeOnBackStackChangedListener(listener)
                }
            }
        manager.findFragmentByTag("SimpleClassName")
    }

    override fun onBackStackChanged() {
        Log.i(TAG, "onBackStackChanged")
    }

    /**
     * A userflow is a visual overview of an application's screens/navigation and structure
     * @param enable: Enables/disables the creation of a userflow when building the application
     */
    fun createUserFlow(enable: Boolean) {

    }

}