package com.woory.presentation.ui.promises

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentPromisesBinding
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.ui.gaming.GamingActivity

class PromisesFragment : BaseFragment<FragmentPromisesBinding>(R.layout.fragment_promises) {

    private val viewModel: PromisesViewModel by viewModels()
    private val promisesAdapter = PromisesAdapter(
        onClickBefore = { },
        onClickOngoing = {
            // Todo :: 테스트용 화면 이동
            if (it != null) {
                GamingActivity.startActivity(requireContext(), it.code)
            }
        },
        onClickEnd = { },
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