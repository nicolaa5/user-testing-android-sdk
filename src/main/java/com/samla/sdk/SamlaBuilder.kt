package com.samla.sdk

import androidx.lifecycle.Lifecycle

interface SamlaBuilder {
    fun withLifeCycle(lifeCycle: Lifecycle): SamlaBuilder
}