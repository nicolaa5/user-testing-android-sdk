package com.example.samla;

import android.app.Activity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

public interface SamlaBuilder {
    SamlaBuilder withLifeCycle(Lifecycle lifeCycle);

}
