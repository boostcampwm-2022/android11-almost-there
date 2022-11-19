package com.woory.almostthere.ui.creatingpromise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.woory.almostthere.BuildConfig
import com.woory.almostthere.databinding.FragmentLocationSearchBinding
import com.woory.almostthere.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSearchFragment : Fragment() {

    private val binding: FragmentLocationSearchBinding by viewBinding(
        FragmentLocationSearchBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapPromiseLocationPick.setSKTMapApiKey(MAP_API_KEY)
    }

    companion object {
        private const val MAP_API_KEY = BuildConfig.MAP_API_KEY
    }
}