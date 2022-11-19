package com.woory.almostthere.ui.dummy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.woory.almostthere.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DummyAlmostPromise : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dummy_almost_promise, container, false)
    }
}