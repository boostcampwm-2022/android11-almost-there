package com.woory.presentation.ui.join

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import com.woory.presentation.R
import com.woory.presentation.databinding.ActivityProfileBinding
import com.woory.presentation.model.PromiseDataModel
import com.woory.presentation.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            binding.textView.text =
                intent?.extras?.getParcelable(PROMISE_KEY, PromiseDataModel::class.java).toString()
        } else {
            binding.textView.text = intent?.extras?.getParcelable(PROMISE_KEY)
        }
    }

    companion object {
        private const val PROMISE_KEY = "PROMISE_KEY"

        fun startActivity(context: Context, promiseDataModel: PromiseDataModel) =
            context.startActivity(Intent(context, ProfileActivity::class.java).apply {
                putExtra(PROMISE_KEY, promiseDataModel)
            })
    }
}