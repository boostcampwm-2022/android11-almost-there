package com.woory.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivitySplashBinding
import com.woory.presentation.ui.BaseActivity
import com.woory.presentation.ui.main.MainActivity
import com.woory.presentation.util.textScaleAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler.postDelayed({
            binding.tvNext.visibility = View.VISIBLE
            binding.tvNext.startAnimation(textScaleAnimation)

            binding.root.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }
}