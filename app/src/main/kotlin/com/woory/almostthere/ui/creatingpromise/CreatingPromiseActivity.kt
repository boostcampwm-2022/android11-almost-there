package com.woory.almostthere.ui.creatingpromise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.woory.almostthere.R

class CreatingPromiseActivity : AppCompatActivity() {

    private val viewModel: CreatingPromiseViewModel by lazy {
        ViewModelProvider(this)[CreatingPromiseViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creating_promise)
    }
}