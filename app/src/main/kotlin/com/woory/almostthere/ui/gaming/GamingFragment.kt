package com.woory.almostthere.ui.gaming

import android.os.Bundle
import android.view.View
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentGamingBinding
import com.woory.almostthere.ui.BaseFragment
import com.woory.almostthere.util.MAP_API_KEY

class GamingFragment : BaseFragment<FragmentGamingBinding>(R.layout.fragment_gaming) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapGaming.setSKTMapApiKey(MAP_API_KEY)
    }
}