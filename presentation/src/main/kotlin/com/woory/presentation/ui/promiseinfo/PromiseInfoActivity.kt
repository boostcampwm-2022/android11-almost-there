package com.woory.presentation.ui.promiseinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityPromiseInfoBinding
import com.woory.presentation.ui.BaseActivity
import com.woory.presentation.util.NO_GAME_CODE_EXCEPTION
import com.woory.presentation.util.PROMISE_CODE_KEY
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.FragmentComponentManager

@AndroidEntryPoint
class PromiseInfoActivity :
    BaseActivity<ActivityPromiseInfoBinding>(R.layout.activity_promise_info) {

    private val gameCode by lazy {
        intent?.getStringExtra(PROMISE_CODE_KEY)
            ?: throw NO_GAME_CODE_EXCEPTION
    }

    private val viewModel: PromiseInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FragmentComponentManager.findActivity(this)
        initToolbar(binding.containerToolbar.toolbar, getString(R.string.promise_info))
        viewModel.setGameCode(gameCode)
    }

    companion object {
        fun startActivity(context: Context, promiseCode: String) =
            context.startActivity(Intent(context, PromiseInfoActivity::class.java).apply {
                putExtra(PROMISE_CODE_KEY, promiseCode)
            })
    }
}