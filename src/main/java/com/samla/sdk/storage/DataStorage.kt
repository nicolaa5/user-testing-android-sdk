package com.samla.sdk.storage

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.samla.sdk.ISamla

object DataStorage {

    lateinit var ISamla: ISamla

    fun setClientActivity(entry : ISamla) {
        ISamla = entry;
    }

    /**
     * Store the screen in sharedpreferences
     */
    fun storeScreen (view : View, id : String) {
        val editor: SharedPreferences.Editor = ISamla.getActivity().getSharedPreferences("Screen", Context.MODE_PRIVATE).edit()
        editor.putString("id", id)
        editor.putInt("x", view.x.toInt())
        editor.putInt("y", view.y.toInt())
        editor.putInt("width", view.width)
        editor.putInt("height", view.height)
        editor.apply()
    }

    /**
     * Store the action in sharedpreferences
     */
    fun storeAction (view : View, id : String) {
        val editor: SharedPreferences.Editor = ISamla.getActivity().getSharedPreferences("Action", Context.MODE_PRIVATE).edit()
        editor.putString("id", id)
        editor.putInt("x", view.x.toInt())
        editor.putInt("y", view.y.toInt())
        editor.putInt("width", view.width)
        editor.putInt("height", view.height)
        editor.apply()
    }
}