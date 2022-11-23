package com.woory.presentation.ui.promises

import android.os.Bundle
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityPromisesBinding
import com.woory.presentation.ui.BaseActivity

class PromisesActivity : BaseActivity<ActivityPromisesBinding>(R.layout.activity_promises) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAppBar()
    }

    private fun setUpAppBar() {
        binding.containerToolbar.toolbar.title = getString(R.string.promises_end)
    }
}