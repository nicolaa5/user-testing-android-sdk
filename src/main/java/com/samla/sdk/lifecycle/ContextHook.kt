package com.samla.sdk.lifecycle

import android.app.Activity
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import com.samla.sdk.R
import com.samla.sdk.Samla
import com.samla.sdk.userinterface.UIHierarchy

/**
 * When a ContentProvider is created, Android will call its onCreate method.
 * This is where the SDK can get a hold of a Context,
 * which it does by calling the getContext method. This Context is safe to hold on to indefinitely.
 *
 * This is also a place that can be used to set up things that need to be active throughout the app's lifetime,
 * such as ActivityLifecycleCallbacks or a UncaughtExceptionHandler
 * You might also initialize a dependency injection framework here.
 *
 * https://firebase.googleblog.com/2016/12/how-does-firebase-initialize-on-android.html
 */
class ContextHook : ContentProvider(){


    override fun onCreate(): Boolean {
        //Samla(context!!)
        return true;
    }

    /**
     * We are misusing the ContextProvider class for it's ability to send the context without the user
     * actually sending it to the SDK. This workaround requires these boilerplate methods to be implemented.
     */
    override fun query(p0: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? {return null}
    override fun query(uri: Uri, projection: Array<out String>?, queryArgs: Bundle?, cancellationSignal: CancellationSignal?): Cursor? {return null}
    override fun insert(p0: Uri, p1: ContentValues?): Uri? { return null}
    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {return -1;}
    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int { return -1}
    override fun getType(p0: Uri): String? {  return null}
}