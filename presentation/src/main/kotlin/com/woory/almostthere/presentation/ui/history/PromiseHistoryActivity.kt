package com.woory.almostthere.presentation.ui.history

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.navArgs
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.databinding.ActivityPromiseHistoryBinding
import com.woory.almostthere.presentation.extension.repeatOnStarted
import com.woory.almostthere.presentation.model.PromiseHistory
import com.woory.almostthere.presentation.model.UiState
import com.woory.almostthere.presentation.ui.BaseActivity
import com.woory.almostthere.presentation.ui.gameresult.GameResultActivity
import com.woory.almostthere.presentation.ui.gaming.GamingActivity
import com.woory.almostthere.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.almostthere.presentation.util.handleLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PromiseHistoryActivity :
    BaseActivity<ActivityPromiseHistoryBinding>(R.layout.activity_promise_history) {

    private val viewModel: PromiseHistoryViewModel by viewModels()

    private val args: PromiseHistoryActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        bindViews()
    }

    private fun initViews() = with(binding) {
        vm = viewModel
        adapter = PromiseHistoryAdapter(onClick = { type, promiseHistory ->
            type ?: return@PromiseHistoryAdapter
            promiseHistory ?: return@PromiseHistoryAdapter

            val code = promiseHistory.promise.code

            when (type) {
                PromiseHistoryViewType.BEFORE -> {
                    PromiseInfoActivity.startActivity(this@PromiseHistoryActivity, code)
                }
                PromiseHistoryViewType.ONGOING -> {
                    GamingActivity.startActivity(this@PromiseHistoryActivity, code)
                }
                PromiseHistoryViewType.END -> {
                    GameResultActivity.startActivity(this@PromiseHistoryActivity, code)
                }
            }
        })

        initToolbar(
            containerToolbar.toolbar,
            args.promiseHistoryType.getTitle(this@PromiseHistoryActivity)
        )
    }

    private fun bindViews() {
        repeatOnStarted {
            viewModel.uiState.collectLatest {
                handleState(it)
            }
        }
    }

    private fun handleState(state: UiState<List<PromiseHistory>?>) = with(binding) {
        when (state) {
            is UiState.Loading -> handleLoading(loadingIndicator, true)
            is UiState.Error -> {
                handleLoading(loadingIndicator, false)
                emptyPromiseTextView.visibility = View.VISIBLE
            }
            is UiState.Success -> {
                handleLoading(loadingIndicator, false)
                emptyPromiseTextView.visibility = View.GONE

                binding.adapter?.run {
                    submitList(state.data)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.adapter = null
    }
}