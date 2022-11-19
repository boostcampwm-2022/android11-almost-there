package com.woory.almostthere.ui

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityViewBindingDelegate<out VB : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    ReadOnlyProperty<AppCompatActivity, VB> {

    private var binding: VB? = null

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): VB =
        binding ?: DataBindingUtil.setContentView<VB>(thisRef, layoutResId).also { binding = it }
}