package com.woory.almostthere.ui.creatingpromise

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ActivityCreatingPromiseBinding
import com.woory.almostthere.ui.ActivityViewBindingDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatingPromiseActivity : AppCompatActivity() {

    private val binding: ActivityCreatingPromiseBinding by ActivityViewBindingDelegate(R.layout.activity_creating_promise)

    private val viewModel: CreatingPromiseViewModel by lazy {
        ViewModelProvider(this)[CreatingPromiseViewModel::class.java]
    }
}