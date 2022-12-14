package com.woory.almostthere.presentation.ui.gaming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.databinding.FragmentCharacterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCharacterBinding? = null
    val binding: FragmentCharacterBinding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_character, container, false)
        return binding.root
    }
}