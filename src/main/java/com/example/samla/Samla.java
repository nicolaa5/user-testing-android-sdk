package com.example.samla;

import android.app.Activity;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import static androidx.lifecycle.Lifecycle.Event.*;

public class Samla implements LifecycleObserver, SamlaBuilder {
    private final static String TAG = Samla.class.getSimpleName();

    private Activity applicationActivity;
    private Lifecycle lifecycle;

    public Samla(Activity activity) {
        this.applicationActivity = activity;
    }

    public static Samla withActivity(Activity activity) {
        return new Samla(activity);
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

    @OnLifecycleEvent(ON_CREATE)
    void onCreated(LifecycleOwner lifecycleOwner) {
        Log.i(TAG, "onCreated");
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
    }

    @OnLifecycleEvent(ON_DESTROY)
    void onDestroy () {
        Log.i(TAG, "onDestroy");
    }

    @OnLifecycleEvent(ON_ANY)
    void onAny(LifecycleOwner source, Lifecycle.Event event) {

    }
}
