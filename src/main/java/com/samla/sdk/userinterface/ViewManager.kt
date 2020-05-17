package com.samla.sdk.userinterface

import android.view.View

class ViewManager(viewToManage: View) {

    init {
        viewToManage.addOnLayoutChangeListener { view, left, top, right, bottom,
                                                 oldLeft, oldTop, oldRight, oldBottom ->

        }
    }

    fun destroy() {

    }
}
