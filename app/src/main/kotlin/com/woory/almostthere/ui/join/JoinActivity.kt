package com.woory.almostthere.ui.join

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityJoinBinding
import com.woory.almostthere.model.mapper.asUiState
import com.woory.almostthere.ui.ActivityViewBindingDelegate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {

    private val binding: ActivityJoinBinding by ActivityViewBindingDelegate(R.layout.activity_join)

    internal val viewModel: JoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            viewModel.promise.collectLatest { promise ->
                promise ?: return@collectLatest

                ProfileActivity.startActivity(this@JoinActivity, promise.asUiState())
            }
        }

        lifecycleScope.launch {
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

    companion object {
        fun startActivity(context: Context) =
            context.startActivity(Intent(context, JoinActivity::class.java))
    }
}