package com.woory.presentation.ui.history

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.navArgs
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityPromiseHistoryBinding
import com.woory.presentation.extension.repeatOnStarted
import com.woory.presentation.model.Promise
import com.woory.presentation.model.UiState
import com.woory.presentation.ui.BaseActivity
import com.woory.presentation.ui.gameresult.GameResultActivity
import com.woory.presentation.ui.gaming.GamingActivity
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.presentation.util.handleLoading
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
        adapter = PromiseHistoryAdapter(onClick = { type, promise ->
            type ?: return@PromiseHistoryAdapter
            promise ?: return@PromiseHistoryAdapter

            when (type) {
                PromiseHistoryViewType.BEFORE -> {
                    PromiseInfoActivity.startActivity(this@PromiseHistoryActivity, promise.code)
                }
                PromiseHistoryViewType.ONGOING -> {
                    GamingActivity.startActivity(this@PromiseHistoryActivity, promise.code)
                }
                PromiseHistoryViewType.END -> {
                    GameResultActivity.startActivity(this@PromiseHistoryActivity, promise.code)
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

    private fun handleState(state: UiState<List<Promise>?>) = with(binding) {
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
                    notifyDataSetChanged()
                }
            }
        }
    }
}