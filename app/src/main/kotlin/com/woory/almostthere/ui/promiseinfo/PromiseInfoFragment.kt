package com.woory.almostthere.ui.promiseinfo

import androidx.fragment.app.Fragment
import com.woory.almostthere.databinding.FragmentPromiseInfoBinding
import com.woory.almostthere.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromiseInfoFragment : Fragment() {

    private val binding: FragmentPromiseInfoBinding by viewBinding(FragmentPromiseInfoBinding::bind)

    private lateinit var viewModel: PromiseInfoViewModel
}