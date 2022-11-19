package com.woory.almostthere.ui.gameresult

import androidx.fragment.app.Fragment
import com.woory.almostthere.databinding.FragmentGameResultBinding
import com.woory.almostthere.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameResultFragment : Fragment() {

    private val binding: FragmentGameResultBinding by viewBinding(FragmentGameResultBinding::bind)
}