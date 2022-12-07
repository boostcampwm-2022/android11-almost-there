package com.woory.presentation.ui.gameresult

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityGameResultBinding
import com.woory.presentation.ui.BaseActivity
import com.woory.presentation.util.PROMISE_CODE_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameResultActivity : BaseActivity<ActivityGameResultBinding>(R.layout.activity_game_result) {
    
    private val viewModel: GameResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpToolbar()
        val gameCode = intent?.getStringExtra(PROMISE_CODE_KEY)
        viewModel.setGameCode(gameCode)
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.containerToolbar.toolbar)
        binding.containerToolbar.toolbar.title = getString(R.string.calculate)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }

    companion object {
        fun startActivity(context: Context, promiseCode: String) =
            context.startActivity(Intent(context, GameResultActivity::class.java).apply {
                putExtra(PROMISE_CODE_KEY, promiseCode)
            })
    }

}