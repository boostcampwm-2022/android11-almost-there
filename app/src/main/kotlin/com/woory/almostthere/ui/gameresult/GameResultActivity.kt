package com.woory.almostthere.ui.gameresult

import androidx.appcompat.app.AppCompatActivity
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityGameResultBinding
import com.woory.almostthere.ui.ActivityViewBindingDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameResultActivity : AppCompatActivity() {

    private val binding: ActivityGameResultBinding by ActivityViewBindingDelegate(R.layout.activity_game_result)
}