package com.woory.almostthere.presentation.ui.join

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.woory.almostthere.presentation.R

import com.woory.almostthere.presentation.databinding.ActivityJoinBinding
import com.woory.almostthere.presentation.extension.repeatOnStarted
import com.woory.almostthere.presentation.model.UiState
import com.woory.almostthere.presentation.ui.BaseActivity
import com.woory.almostthere.presentation.util.handleLoading
import com.woory.almostthere.presentation.util.showSnackBar
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