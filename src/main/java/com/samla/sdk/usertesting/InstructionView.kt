package com.samla.sdk.usertesting

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.samla.sdk.R

class InstructionView(context: Context, attrs: AttributeSet): View(context, attrs) {

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.InstructionView)
        val id: String? = attributes.getString(R.styleable.InstructionView_id)
        attributes.recycle()
    }

}