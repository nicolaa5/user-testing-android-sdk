package com.samla.sdk;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.samla.sdk.analytics.ActivityManager;
import com.samla.sdk.analytics.ViewManager;
import com.samla.sdk.analytics.funnel.FunnelManager;
import com.samla.sdk.userinterface.UserInterfaceHierarchy;

import static androidx.lifecycle.Lifecycle.Event.*;

public class Samla implements LifecycleObserver, SamlaBuilder, FragmentManager.OnBackStackChangedListener {
    private final static String TAG = Samla.class.getSimpleName();

    private Activity applicationActivity;
    private Lifecycle lifecycle;
    private FunnelManager funnelManager;
    private ActivityManager activityManager;
    private static boolean userFlowCreationEnabled = true;

    FirebaseAnalytics firebaseAnalytics;
    FirebaseAppLifecycleListener firebaseAppLifecycleListener;
    FirebaseApp firebaseApp;
    FirebaseOptions firebaseOptions;

    public Samla(Activity activity) {
        this.applicationActivity = activity;
        funnelManager = new FunnelManager(applicationActivity);
        activityManager = new ActivityManager(applicationActivity);
        //applicationActivity.addOnBackStackChangedListener(this);
    }

    public static Samla withActivity(Activity activity) {
        return new Samla(activity);
    }

    /**
     * A userflow is a visual overview of an application's screens/navigation and structure
     * @param enable: Enables/disables the creation of a userflow when building the application
     */
    public static void createUserFlow (Boolean enable) {
           userFlowCreationEnabled = enable;
    }

    @Override
    public SamlaBuilder withLifeCycle(Lifecycle lifeCycle) {
        this.lifecycle = lifeCycle;
        this.lifecycle.addObserver(this);
        return this;
    }

    public Activity getApplicationActivity() {
        return applicationActivity;
    }

    public FunnelManager getFunnelManager() {
        return funnelManager;
    }

    @OnLifecycleEvent(ON_CREATE)
    void onCreated(LifecycleOwner lifecycleOwner) {
        Log.i(TAG, "onCreated");
        UserInterfaceHierarchy.setMenuListener(applicationActivity.findViewById(android.R.id.content));
    }

    @OnLifecycleEvent(ON_RESUME)
    void onResume() {
        Log.i(TAG, "onResume");
    }

    @OnLifecycleEvent(ON_PAUSE)
    void onPause () {
        Log.i(TAG, "onPause");
    }

    @OnLifecycleEvent(ON_STOP)
    void onStop () {
        Log.i(TAG, "onStop");

        String hierarchy = UserInterfaceHierarchy.logViewHierarchy(applicationActivity);
        Log.i(TAG, "UI Hierarchy: " + hierarchy);

        UserInterfaceHierarchy.takeScreenShot(applicationActivity);

        if (userFlowCreationEnabled) {
            // Todo: Testing to send
        }

    }

    @OnLifecycleEvent(ON_DESTROY)
    void onDestroy () {
        Log.i(TAG, "onDestroy");
    }

    @OnLifecycleEvent(ON_ANY)
    void onAny(LifecycleOwner source, Lifecycle.Event event) {

    }

    void test () {
        FragmentManager manager = new FragmentManager() {
            @Nullable
            @Override
            public Fragment findFragmentByTag(@Nullable String tag) {
                return super.findFragmentByTag(tag);
            }

            @Override
            public void addOnBackStackChangedListener(@NonNull OnBackStackChangedListener listener) {
                super.addOnBackStackChangedListener(listener);
            }

            @Override
            public void removeOnBackStackChangedListener(@NonNull OnBackStackChangedListener listener) {
                super.removeOnBackStackChangedListener(listener);
            }
        };
        manager.findFragmentByTag("SimpleClassName");


    }

    @Override
    public void onBackStackChanged() {
        Log.i(TAG, "onBackStackChanged");
    }
}
