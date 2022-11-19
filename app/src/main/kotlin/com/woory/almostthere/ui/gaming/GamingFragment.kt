package com.woory.almostthere.ui.gaming

import androidx.fragment.app.Fragment
import com.woory.almostthere.databinding.FragmentGamingBinding
import com.woory.almostthere.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamingFragment : Fragment() {

    private val binding: FragmentGamingBinding by viewBinding(FragmentGamingBinding::bind)
}