package com.samla.sdk

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
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
import com.samla.sdk.userinterface.UIAnalyzer
import com.samla.sdk.userinterface.UIHierarchy
import eu.bolt.screenshotty.Screenshot
import eu.bolt.screenshotty.ScreenshotManager
import eu.bolt.screenshotty.ScreenshotManagerBuilder
import eu.bolt.screenshotty.util.ScreenshotFileSaver
import java.io.File


object Samla  : LifecycleObserver, SamlaBuilder, FragmentManager.OnBackStackChangedListener, ISamla {
    private val TAG = Samla::class.java.simpleName

    lateinit var mContext : Context
    lateinit var mActivity: Activity
    lateinit var mLifeCycle: Lifecycle;

    lateinit var mScreenshotManager : ScreenshotManager
    val REQUEST_SCREENSHOT_PERMISSION = 888

    lateinit var mDialogContext : Context
    lateinit var mDialogActivity: Activity
    lateinit var mDialogLifeCycle: Lifecycle;

    lateinit var firebaseAnalytics: FirebaseAnalytics;
    lateinit var firebaseAppLifecycleListener: FirebaseAppLifecycleListener;
    lateinit var firebaseApp: FirebaseApp;
    lateinit var firebaseOptions: FirebaseOptions;

    fun init (context : Context) {
        mContext  = context
        mActivity = context as Activity

        FunnelManager.setClientActivity(this);
        ActivityManager.setClientActivity(this);
        DataStorage.setClientActivity(this);
        Analytics.setClientActivity(this);

        mScreenshotManager = ScreenshotManagerBuilder(mActivity)
            .withPermissionRequestCode(REQUEST_SCREENSHOT_PERMISSION) //optional, 888 is the default
            .build()

        //Await the moment the UI is rendered
        mActivity.window.decorView.doOnLayout  {
            //Get the UI hierarchy
            Log.i(TAG, "UI Hierarchy: " + UIHierarchy.logViewHierarchy(mActivity.window.decorView.rootView))


            //Set UI hierarchy change listener
            UIAnalyzer.setViewLayoutChangedListener (mActivity.window.decorView.rootView as ViewGroup, true, true) { view ->
                Log.i(TAG, "LayoutChanged: " + UIHierarchy.logViewHierarchy(mActivity.window.decorView.rootView))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    UIHierarchy.takeScreenshot(mActivity.window.decorView.rootView, mActivity) { bitmap : Bitmap, result : Int  ->
                        when (result) {
                            PixelCopy.SUCCESS -> {
                                UIHierarchy.storeScreenshot(bitmap, mActivity, 100)
                            }
                            else -> {
                                Log.i(TAG, "PixelCopy Error: " + result)
                                val screen : Bitmap = UIHierarchy.takeScreenshot(mActivity.window.decorView.rootView)
                                UIHierarchy.storeScreenshot(screen, mActivity, 100)
                            }
                        }
                    }
                }
                else {
                    val screen : Bitmap = UIHierarchy.takeScreenshot(mActivity.window.decorView.rootView)
                    UIHierarchy.storeScreenshot(screen, mActivity, 100)
                }
            }

            UIAnalyzer.setViewChildChangedListener(mActivity.window.decorView.rootView as ViewGroup) { view ->
                Log.i(TAG, "uiHierarchyChangedListener")

                UIAnalyzer.setViewClickListeners(view, false) { view ->
                    Log.i(TAG, "ClickedView (new): " + view.javaClass.simpleName)
                }
            }

            //Set Listeners to all interactable elements
            UIAnalyzer.setViewClickListeners(mActivity.window.decorView.rootView, true) { view ->
                Log.i(TAG, "ClickedView: " + view.javaClass.simpleName)
            }

        }
    }

    fun init (dialog : Dialog) {
        mDialogContext = dialog.context
        mDialogActivity = unwrap(mDialogContext)

        val screenshotResult = mScreenshotManager.makeScreenshot()
        val subscription = screenshotResult.observe(
            onSuccess = { writeToFile(it) },
            onError = { }
        )
    }

    fun writeToFile(screenshot: Screenshot): File {
        val fileSaver = ScreenshotFileSaver.create(Bitmap.CompressFormat.PNG)
        val targetFile = File(mContext.filesDir, "screenshot")
        fileSaver.saveToFile(targetFile, screenshot)
        return targetFile
    }

    private fun unwrap(context: Context): Activity {
        var mContext : Context = context
        while (mContext !is Activity && mContext is ContextWrapper) {
            mContext = mContext.baseContext
        }
        return mContext as Activity
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
        val hierarchy = UIHierarchy.logViewHierarchy(mActivity.window.decorView.rootView)
        Log.i(TAG, "UI Hierarchy onStop: $hierarchy")
        UIHierarchy.takeScreenshot(mActivity)
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
            mActivity.window.decorView.rootView
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