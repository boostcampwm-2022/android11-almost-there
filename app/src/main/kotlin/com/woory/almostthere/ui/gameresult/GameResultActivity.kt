package com.woory.almostthere.ui.gameresult

import android.os.Bundle
import android.os.PersistableBundle
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityGameResultBinding
import com.woory.almostthere.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameResultActivity : BaseActivity<ActivityGameResultBinding>(R.layout.activity_game_result) {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}