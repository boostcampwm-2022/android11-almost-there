package com.woory.presentation.ui.promiseinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityPromiseInfoBinding
import com.woory.presentation.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.FragmentComponentManager

@AndroidEntryPoint
class PromiseInfoActivity :
    BaseActivity<ActivityPromiseInfoBinding>(R.layout.activity_promise_info) {

    private val gameCode by lazy {
        intent?.getStringExtra(PROMISE_KEY) ?: throw IllegalArgumentException("참여 코드가 없습니다.")
    }

    private val viewModel: PromiseInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FragmentComponentManager.findActivity(this)
        setUpAppBar()
        viewModel.setGameCode(gameCode)
    }

    private fun setUpAppBar() {
        binding.containerToolbar.toolbar.title = getString(R.string.promise_info)
    }

    companion object {
        private const val PROMISE_KEY = "PROMISE_KEY"

        fun startActivity(context: Context, promiseKey: String) =
            context.startActivity(Intent(context, PromiseInfoActivity::class.java).apply {
                putExtra(PROMISE_KEY, promiseKey)
            })
    }
}