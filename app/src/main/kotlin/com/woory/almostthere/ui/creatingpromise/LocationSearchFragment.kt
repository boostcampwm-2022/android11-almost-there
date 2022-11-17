package com.woory.almostthere.ui.creatingpromise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentLocationSearchBinding

class LocationSearchFragment : Fragment() {

    private var _binding: FragmentLocationSearchBinding? = null
    private val binding: FragmentLocationSearchBinding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_location_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapPromiseLocationPick.setSKTMapApiKey(MAP_API_KEY)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MAP_API_KEY = "l7xx7f81c09fe3364929a9c4d978e35b2fe8"
    }
}