package com.woory.almostthere.presentation.ui.join

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.background.alarm.AlarmFunctions
import com.woory.almostthere.presentation.databinding.ActivityProfileBinding
import com.woory.almostthere.presentation.extension.repeatOnStarted
import com.woory.almostthere.presentation.model.PromiseAlarm
import com.woory.almostthere.presentation.model.UiState
import com.woory.almostthere.presentation.ui.BaseActivity
import com.woory.almostthere.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.almostthere.presentation.util.SoftKeyboardUtils
import com.woory.almostthere.presentation.util.handleLoading
import com.woory.almostthere.presentation.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    private lateinit var softKeyboardUtil: SoftKeyboardUtils

    private val viewModel: ProfileViewModel by viewModels()

    private val alarmFunctions = AlarmFunctions(this)

    private val code: String? by lazy { intent.getStringExtra(PROMISE_CODE_KEY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel

        initToolbar(binding.containerToolbar.toolbar, getString(R.string.join))
        bindViews()
    }

    private fun bindViews() = with(binding) {
        joinButton.setOnClickListener {
            code?.let { code ->
                viewModel.join(code)
            }
        }

        repeatOnStarted {
            viewModel.uiState.collectLatest { uiState ->
                handleState(uiState)
            }
        }

        softKeyboardUtil = SoftKeyboardUtils(window, onShowKeyboard = { keyboardHeight ->
            scrollView.apply {
                smoothScrollTo(scrollX, scrollY + keyboardHeight)
            }
        })
    }

    private fun handleState(state: UiState<PromiseAlarm>) = when (state) {
        is UiState.Loading -> handleLoading(binding.loadingIndicator, true)
        is UiState.Error -> {
            handleLoading(binding.loadingIndicator, false)

            showSnackBar(binding.root, getString(state.errorType.messageResId))
        }
        is UiState.Success -> {
            handleLoading(binding.loadingIndicator, false)

            registerAlarm(state.data)
        }
    }

    private fun registerAlarm(promiseAlarm: PromiseAlarm) {
        alarmFunctions.registerAlarm(promiseAlarm)
        PromiseInfoActivity.startActivity(this@ProfileActivity, promiseAlarm.promiseCode)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        softKeyboardUtil.detachKeyboardListeners()
    }

    companion object {
        const val PROMISE_CODE_KEY = "PROMISE_CODE_KEY"

        fun startActivity(context: Context, promiseCode: String) =
            context.startActivity(Intent(context, ProfileActivity::class.java).apply {
                putExtra(PROMISE_CODE_KEY, promiseCode)
            })
    }
}