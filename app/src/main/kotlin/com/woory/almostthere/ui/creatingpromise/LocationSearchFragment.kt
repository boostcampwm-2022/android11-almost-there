package com.woory.almostthere.ui.creatingpromise

import android.os.Bundle
import android.view.View
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentLocationSearchBinding
import com.woory.almostthere.ui.BaseFragment
import com.woory.almostthere.util.MAP_API_KEY

class LocationSearchFragment :
    BaseFragment<FragmentLocationSearchBinding>(R.layout.fragment_location_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapPromiseLocationPick.setSKTMapApiKey(MAP_API_KEY)
    }
}