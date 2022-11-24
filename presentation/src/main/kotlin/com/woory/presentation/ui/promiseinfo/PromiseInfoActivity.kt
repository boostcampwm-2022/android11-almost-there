package com.woory.presentation.ui.promiseinfo

import android.content.Context
import android.content.Intent
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

    companion object {
        private const val PROMISE_CODE_KEY = "PROMISE_CODE_KEY"

        fun startActivity(context: Context, promiseCode: String) =
            context.startActivity(Intent(context, PromiseInfoActivity::class.java).apply {
                putExtra(PROMISE_CODE_KEY, promiseCode)
            })
    }
}