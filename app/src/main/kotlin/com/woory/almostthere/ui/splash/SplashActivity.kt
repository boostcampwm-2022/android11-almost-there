package com.woory.almostthere.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivitySplashBinding
import com.woory.almostthere.ui.MainActivity

class SplashActivity : AppCompatActivity() {

    private val binding: ActivitySplashBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    private val scaleAnimation = ScaleAnimation(
        1f, 1.2f, 1f, 1.2f,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
        repeatCount = Animation.INFINITE
        duration = 1000
        repeatMode = Animation.REVERSE
    }

    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler.postDelayed({
            binding.tvNext.visibility = View.VISIBLE
            binding.tvNext.startAnimation(scaleAnimation)
        }, 2000)

        binding.tvNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}