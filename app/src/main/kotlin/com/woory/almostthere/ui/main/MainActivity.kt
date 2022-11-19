package com.woory.almostthere.ui.main

import androidx.appcompat.app.AppCompatActivity
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityMainBinding
import com.woory.almostthere.ui.ActivityViewBindingDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by ActivityViewBindingDelegate(R.layout.activity_main)
}