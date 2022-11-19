package com.woory.almostthere.ui.gaming

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.woory.almostthere.databinding.FragmentCharacterBinding
import com.woory.almostthere.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFragment : BottomSheetDialogFragment() {

    private val binding: FragmentCharacterBinding by viewBinding(FragmentCharacterBinding::bind)

    companion object {
        const val TAG = "CharacterFragment"
    }
}