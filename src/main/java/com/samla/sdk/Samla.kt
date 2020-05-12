package com.samla.sdk

import android.app.Activity
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
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
import com.samla.sdk.userflow.funnel.FunnelManager
import com.samla.sdk.userinterface.ActivityManager
import com.samla.sdk.userinterface.UIHierarchy

class Samla() : ContentProvider(),  LifecycleObserver, SamlaBuilder, FragmentManager.OnBackStackChangedListener, ISamla {

    lateinit var mContext : Context
    lateinit var mActivity: Activity
    lateinit var mLifeCycle: Lifecycle;

    lateinit var firebaseAnalytics: FirebaseAnalytics;
    lateinit var firebaseAppLifecycleListener: FirebaseAppLifecycleListener;
    lateinit var firebaseApp: FirebaseApp;
    lateinit var firebaseOptions: FirebaseOptions;


    init {
        FunnelManager.setClientActivity(this);
        ActivityManager.setClientActivity(this);
        DataStorage.setClientActivity(this);
    }

    override fun getActivity(): Activity {
        return mActivity
    }

    /**
     * onCreate -> ContextProvider
     * When a ContentProvider is created, Android will call its onCreate method.
     * This is where the SDK can get a hold of a Context,
     * which it does by calling the getContext method. This Context is safe to hold on to indefinitely.
     *
     * This is also a place that can be used to set up things that need to be active throughout the app's lifetime,
     * such as ActivityLifecycleCallbacks or a UncaughtExceptionHandler
     * You might also initialize a dependency injection framework here.
     *
     * https://firebase.googleblog.com/2016/12/how-does-firebase-initialize-on-android.html
     */
    override fun onCreate(): Boolean {
        mContext  = context!!
        mActivity = context!! as Activity

        var activities : Activity = mContext as Activity;

        UIHierarchy.setMenuListener(
            mActivity.findViewById(
                R.id.content
            )
        )

        return true;
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
        if (userFlowCreationEnabled) { // Todo: Testing to send
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.i(TAG, "onDestroy")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(source: LifecycleOwner?, event: Lifecycle.Event?) {
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

    companion object {
        private val TAG = Samla::class.java.simpleName
        private var userFlowCreationEnabled = true
        fun withActivity(activity: Activity): Samla {
            return Samla()
        }

        /**
         * A userflow is a visual overview of an application's screens/navigation and structure
         * @param enable: Enables/disables the creation of a userflow when building the application
         */
        fun createUserFlow(enable: Boolean) {
            userFlowCreationEnabled = enable
        }
    }

    /**
     * We are misusing the ContextProvider class for it's ability to send the context without the user
     * actually sending it to the SDK. This workaround requires these boilerplate methods to be implemented.
     */
    override fun query(p0: Uri, p1: Array<out String>?, p2: String?,p3: Array<out String>?, p4: String?): Cursor? {return null}
    override fun query( uri: Uri, projection: Array<out String>?, queryArgs: Bundle?, cancellationSignal: CancellationSignal?): Cursor? {return null}
    override fun insert(p0: Uri, p1: ContentValues?): Uri? { return null}
    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {return -1;}
    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int { return -1}
    override fun getType(p0: Uri): String? {  return null}
}