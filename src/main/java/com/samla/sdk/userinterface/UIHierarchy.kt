package com.samla.sdk.userinterface

import android.R
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.util.Log
import android.util.Pair
import android.view.PixelCopy
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream
import java.util.*

object UIHierarchy {
    private val TAG = UIHierarchy::class.java.simpleName

    /** Print into log view hierarchy.  */
    fun logViewHierarchy(root: View): String {
        val output = StringBuilder(8192).append("\n")
        val r = root.resources

        //The stack stores the full UI hierarchy in Pair<String, View>
        val stack = Stack<Pair<String, View>>()

        //Create root pair
        stack.push(Pair.create("", root))

        while (!stack.empty()) {
            val pair = stack.pop()
            val v = pair.second

            //UI component information
            val tags : String = " tag: " + v.tag
            val className : String = v.javaClass.simpleName

            //UI visual details
            val dimensions : String = " width: " + v.width + " height: " + v.height
            val location : String = " X: " + v.x + " Y: " + v.y
            val pivot : String = " pivotX: " + v.pivotX + " pivotY: " + v.pivotY
            val color : String = if (getBackgroundColor(v) != null) " color: " + getBackgroundColor( v ) else ""

            //Log line
            val isLastOnLevel : Boolean = stack.empty() || pair.first != stack.peek().first
            val logDisplay : String = "" + pair.first + if (isLastOnLevel) "└── " else "├── "
            val line : String = logDisplay + className + tags + location + dimensions + color + " id=" + v.id + resolveIdToName(r,v)

            output.append(line).append("\n")
            if (v is ViewGroup) {
                val vg = v
                for (i in vg.childCount - 1 downTo 0) {
                    stack.push(
                        Pair.create(
                            pair.first.toString() + if (isLastOnLevel) "    " else "│   ",
                            vg.getChildAt(i)
                        )
                    )
                }
            }

            if (v is Button) {

            }
        }
        return output.toString()
    }

    fun setMenuListener(v: View) {
        v.setOnCreateContextMenuListener { contextMenu, view, contextMenuInfo ->
            for (i in 0 until contextMenu.size()) {
                Log.i("Menu: ", i.toString() + ": " + contextMenu.getItem(i))
            }
        }
    }

    /**
     * @see [Lookup resource name](https://stackoverflow.com/questions/10137692/how-to-get-resource-name-from-resource-id)
     * /* package */
     */
    private fun resolveIdToName(r: Resources?, v: View): String {
        return if (null == r) "" else try {
            " / " + r.getResourceEntryName(v.id)
        } catch (ignored: Throwable) {
            ""
        }
    }

    fun takeScreenshot(activity: Activity): Bitmap {
        return Bitmap.createBitmap(activity.window.decorView.width, activity.window.decorView.height, Bitmap.Config.ARGB_8888)
    }

    fun takeScreenshot(view: View): Bitmap {
        return Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun takeScreenshot(view: View, activity: Activity, callback: (Bitmap, Int) -> Unit) {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val locationOfViewInWindow = IntArray(2)
            view.getLocationInWindow(locationOfViewInWindow)
            try {
                activity.window?.let { window ->
                    Log.i(TAG, "window: " + window)
                    PixelCopy.request(window, Rect(locationOfViewInWindow[0], locationOfViewInWindow[1], locationOfViewInWindow[0] + view.width, locationOfViewInWindow[1] + view.height), bitmap, { copyResult ->
                        callback(bitmap, copyResult)
                    }, Handler())
                }
            } catch (e: IllegalArgumentException) {
                // PixelCopy may throw IllegalArgumentException, make sure to handle it
                e.printStackTrace()
            }
    }

    fun storeScreenshot (bitmap: Bitmap, activity: Activity, compressQuality : Int) : File {
        val now = Date()
        val image = File(activity.filesDir.toString() + "/" + now + ".png")
        val outputStream = FileOutputStream(image)
        bitmap.compress(Bitmap.CompressFormat.PNG, compressQuality, outputStream)
        outputStream.flush()
        outputStream.close()

        return image;
    }

    fun storeScreenshot (bitmap : Bitmap, path : String, compressQuality : Int) : File {
        val image = File(path)
        val outputStream = FileOutputStream(image)
        bitmap.compress(Bitmap.CompressFormat.PNG, compressQuality, outputStream)
        outputStream.flush()
        outputStream.close()

        return image;
    }

    fun openScreenshot(activity: Activity, imageFile: File) {
        val uri = FileProvider.getUriForFile(
            activity,
            activity.applicationContext.packageName + ".fileprovider",
            imageFile
        )
        val intent = Intent()
            .setAction(Intent.ACTION_VIEW)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .setDataAndType(uri, "image/*")
        activity.startActivity(intent)
    }

    private fun getBackgroundColor(view: View): String? {
        val drawable = view.background
        if (drawable is ColorDrawable) {
            return String.format("#%06X", 0xFFFFFF and drawable.color)
        }
        return null
    }
}