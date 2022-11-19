package com.woory.almostthere.ui.gaming

import androidx.appcompat.app.AppCompatActivity
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityGameResultBinding
import com.woory.almostthere.ui.ActivityViewBindingDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamingActivity : AppCompatActivity() {

    private val binding: ActivityGameResultBinding by ActivityViewBindingDelegate(R.layout.activity_gaming)
}