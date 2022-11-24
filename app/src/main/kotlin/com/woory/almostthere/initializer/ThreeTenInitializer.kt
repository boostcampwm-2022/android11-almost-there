package com.woory.almostthere.initializer

import android.content.Context
import androidx.startup.Initializer
import com.jakewharton.threetenabp.AndroidThreeTen

class ThreeTenInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        AndroidThreeTen.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}