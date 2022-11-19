package com.woory.almostthere.ui.promiseinfo

import androidx.appcompat.app.AppCompatActivity
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityPromiseInfoBinding
import com.woory.almostthere.ui.ActivityViewBindingDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromiseInfoActivity : AppCompatActivity() {

    private val binding: ActivityPromiseInfoBinding by ActivityViewBindingDelegate(R.layout.activity_promise_info)
}