package com.woory.almostthere.ui.promises

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentPromisesBinding
import com.woory.almostthere.ui.BaseFragment

class PromisesFragment: BaseFragment<FragmentPromisesBinding>(R.layout.fragment_promises) {

    private val viewModel: PromisesViewModel by viewModels()
    private val promisesAdapter = PromisesAdapter(
        onClickEnd = {  }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        binding.rvPromises.adapter = promisesAdapter
        promisesAdapter.submitList(viewModel.dummyPromises)
    }
}