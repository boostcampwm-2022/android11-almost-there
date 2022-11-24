package com.woory.presentation.ui.creatingpromise

import android.os.Bundle
import androidx.activity.viewModels
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityCreatingPromiseBinding
import com.woory.presentation.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatingPromiseActivity :
    BaseActivity<ActivityCreatingPromiseBinding>(R.layout.activity_creating_promise) {

    private val viewModel: CreatingPromiseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAppBar()
    }

    private fun setUpAppBar() {
        binding.containerToolbar.toolbar.title = getString(R.string.promise_creation)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }
}