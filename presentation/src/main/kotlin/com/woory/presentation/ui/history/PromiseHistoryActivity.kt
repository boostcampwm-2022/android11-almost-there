package com.woory.presentation.ui.history

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityPromiseHistoryBinding
import com.woory.presentation.ui.BaseActivity
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromiseHistoryActivity :
    BaseActivity<ActivityPromiseHistoryBinding>(R.layout.activity_promise_history) {

    internal val viewModel: PromiseHistoryViewModel by viewModels()

    private val args: PromiseHistoryActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            vm = viewModel
            adapter = PromiseHistoryAdapter(onClick = { type, promise ->
                type ?: return@PromiseHistoryAdapter
                promise ?: return@PromiseHistoryAdapter

                when (type) {
                    PromiseHistoryViewType.BEFORE -> {
                        PromiseInfoActivity.startActivity(this@PromiseHistoryActivity, promise.code)
                    }
                    PromiseHistoryViewType.ONGOING -> {
                        // FIXME: 게임 진행중 화면으로 이동하도록 수정
                        PromiseInfoActivity.startActivity(this@PromiseHistoryActivity, promise.code)
                    }
                    PromiseHistoryViewType.END -> {
                        // FIXME: 게임 결과 화면으로 이동하도록 수정
                        PromiseInfoActivity.startActivity(this@PromiseHistoryActivity, promise.code)
                    }
                }
            })
        }

        initToolbar()
        bindViews()
    }

    private fun initToolbar() = with(binding.containerToolbar.toolbar) {
        setSupportActionBar(this)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = args.promiseHistoryType.getTitle(this@PromiseHistoryActivity)
        }
    }

    private fun bindViews() = with(binding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.promiseList.collectLatest { list ->
                    list ?: return@collectLatest

                    adapter?.submitList(list)
                }
            }
        }
    }
}