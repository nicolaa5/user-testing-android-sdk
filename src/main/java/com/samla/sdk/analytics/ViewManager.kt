package com.samla.sdk.analytics

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class ViewManager(viewToManage: View) {

    init {
        viewToManage.addOnLayoutChangeListener { view, left, top, right, bottom,
                                                 oldLeft, oldTop, oldRight, oldBottom ->

        }
    }

    fun destroy() {

    }
}
