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

        initToolbar(binding.containerToolbar.toolbar, getString(R.string.calculate))
        val gameCode = intent?.getStringExtra(PROMISE_CODE_KEY)
        viewModel.setGameCode(gameCode)
    }

    companion object {
        fun startActivity(context: Context, promiseCode: String) =
            context.startActivity(Intent(context, GameResultActivity::class.java).apply {
                putExtra(PROMISE_CODE_KEY, promiseCode)
            })
    }

}