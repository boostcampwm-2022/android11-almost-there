package com.woory.presentation.ui.join

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityJoinBinding
import com.woory.presentation.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinActivity : BaseActivity<ActivityJoinBinding>(R.layout.activity_join) {

    internal val viewModel: JoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this@JoinActivity
        binding.vm = viewModel

        initToolbar()
        bindViews()
    }

    private fun initToolbar() = with(binding) {
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    private fun bindViews() = with(binding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.promise.collectLatest { promise ->
                    promise ?: return@collectLatest

                    ProfileActivity.startActivity(this@JoinActivity, promise.data)
                    finish()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorType.collectLatest { codeState ->
                    codeState ?: return@collectLatest

                    Snackbar.make(
                        binding.root,
                        codeState.getMessage(this@JoinActivity),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}