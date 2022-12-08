package com.woory.presentation.ui.join

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityJoinBinding
import com.woory.presentation.extension.repeatOnStarted
import com.woory.presentation.model.UiState
import com.woory.presentation.ui.BaseActivity
import com.woory.presentation.util.handleLoading
import com.woory.presentation.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class JoinActivity : BaseActivity<ActivityJoinBinding>(R.layout.activity_join) {

    private val viewModel: JoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel

        initToolbar(binding.containerToolbar.toolbar, getString(R.string.join))
        bindViews()
    }

    private fun bindViews() = with(binding) {
        repeatOnStarted {
            viewModel.uiState.collectLatest { uiState ->
                handleState(uiState)
            }
        }
    }

    private fun handleState(state: UiState<String>) = when (state) {
        is UiState.Loading -> handleLoading(binding.loadingIndicator, true)
        is UiState.Error -> {
            handleLoading(binding.loadingIndicator, false)
            showSnackBar(binding.root, getString(R.string.invalid_invite_code))
        }
        is UiState.Success -> {
            handleLoading(binding.loadingIndicator, false)
            ProfileActivity.startActivity(this@JoinActivity, state.data)
        }
    }

    companion object {
        fun startActivity(context: Context) =
            context.startActivity(Intent(context, JoinActivity::class.java))
    }
}