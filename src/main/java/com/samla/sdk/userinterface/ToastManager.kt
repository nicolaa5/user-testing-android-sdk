package com.samla.sdk.userinterface

import android.app.Activity
import android.widget.Toast

object ToastManager {
    @JvmStatic
    fun makeToast(activity: Activity, message: String?) {
        activity.runOnUiThread { Toast.makeText(activity, message, Toast.LENGTH_SHORT).show() }
    }
}