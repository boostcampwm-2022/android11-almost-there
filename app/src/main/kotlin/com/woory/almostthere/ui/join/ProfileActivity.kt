package com.woory.almostthere.ui.join

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityProfileBinding
import com.woory.almostthere.model.PromiseUiState
import com.woory.almostthere.ui.ActivityViewBindingDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by ActivityViewBindingDelegate(R.layout.activity_profile)

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        binding.textView.text = intent?.extras?.getParcelable(PROMISE_KEY)
    }

    companion object {
        private const val PROMISE_KEY = "PROMISE_KEY"

        fun startActivity(context: Context, promiseUiState: PromiseUiState) =
            context.startActivity(Intent(context, ProfileActivity::class.java).apply {
                putExtra(PROMISE_KEY, promiseUiState)
            })
    }
}