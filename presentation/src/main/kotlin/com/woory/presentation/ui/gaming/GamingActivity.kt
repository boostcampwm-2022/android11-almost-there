package com.woory.presentation.ui.gaming

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.skt.tmap.overlay.TMapMarkerItem
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityGameResultBinding
import com.woory.presentation.ui.BaseActivity
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.presentation.ui.promiseinfo.PromiseInfoViewModel
import com.woory.presentation.util.PROMISE_CODE_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamingActivity : BaseActivity<ActivityGameResultBinding>(R.layout.activity_gaming){
    private val gameCode by lazy {
        intent?.getStringExtra(PROMISE_CODE_KEY) ?: throw IllegalArgumentException("참여 코드가 없습니다.")
    }

    private val viewModel: GamingViewModel by viewModels()

    private val bitmap by lazy {
        ContextCompat.getDrawable(this, R.drawable.bg_speech_bubble)?.toBitmap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setGameCode(gameCode)
        bitmap?.let {
            viewModel.setDefaultMarker(it)
        }

        // TODO :: 서비스에 Bind 하기
    }

    companion object {
        fun startActivity(context: Context, promiseCode: String) =
            context.startActivity(Intent(context, GamingActivity::class.java).apply {
                putExtra(PROMISE_CODE_KEY, promiseCode)
            })
    }
}