package com.woory.almostthere.ui.creatingpromise

import androidx.lifecycle.ViewModelProvider
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityCreatingPromiseBinding
import com.woory.almostthere.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatingPromiseActivity :
    BaseActivity<ActivityCreatingPromiseBinding>(R.layout.activity_creating_promise) {

    private val viewModel: CreatingPromiseViewModel by lazy {
        ViewModelProvider(this)[CreatingPromiseViewModel::class.java]
    }
}