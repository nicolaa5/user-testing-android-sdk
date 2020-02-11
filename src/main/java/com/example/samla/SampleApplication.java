package com.example.samla;

import androidx.appcompat.app.AppCompatActivity;

public class SampleApplication extends AppCompatActivity {


   public SampleApplication () {
       Samla.withActivity(this)
            .withLifeCycle(getLifecycle());
   }
}
