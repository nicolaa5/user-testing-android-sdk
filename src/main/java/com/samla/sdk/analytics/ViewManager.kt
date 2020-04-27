package com.samla.sdk.analytics

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class ViewManager(activity : Activity, viewToManage: View) {

    init {
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

        viewToManage.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->

        }
    }

    fun destroy() {

    }
}
