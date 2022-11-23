package com.woory.presentation.ui.promiseinfo

import android.os.Bundle
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityPromiseInfoBinding
import com.woory.presentation.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromiseInfoActivity :
    BaseActivity<ActivityPromiseInfoBinding>(R.layout.activity_promise_info) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAppBar()
    }

    private fun setUpAppBar() {
        binding.containerToolbar.toolbar.title = getString(R.string.promise_info)
    }
}