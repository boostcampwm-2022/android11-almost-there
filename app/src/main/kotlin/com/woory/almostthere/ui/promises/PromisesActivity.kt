package com.woory.almostthere.ui.promises

import android.os.Bundle
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityPromisesBinding
import com.woory.almostthere.ui.BaseActivity

class PromisesActivity : BaseActivity<ActivityPromisesBinding>(R.layout.activity_promises) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAppBar()
    }

    private fun setUpAppBar() {
        binding.containerToolbar.toolbar.title = getString(R.string.promises_end)
    }
}