package com.woory.presentation.ui.gameresult

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentGameResultBinding
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.ui.customview.topitemresize.TopItemResizeScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameResultFragment : BaseFragment<FragmentGameResultBinding>(R.layout.fragment_game_result) {

    private val viewModel: GameResultViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpBind()
        setUpClickListener()
        setUpRecyclerView()
        observeData()
    }

    private fun setUpToolbar() {
        (requireActivity() as GameResultActivity).supportActionBar?.hide()
    }

    private fun setUpClickListener() {
        binding.btnCalculate.setOnClickListener {
            findNavController().navigate(R.id.nav_calculate_frag)
        }

        binding.btnExit.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun setUpBind() {
        binding.vm = viewModel
    }

    private fun setUpRecyclerView() {
        binding.rvRanking.run {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = linearLayoutManager

            adapter = UserRankingAdapter()

            addOnScrollListener(TopItemResizeScrollListener(linearLayoutManager))
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.errorEvent.collect {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                        delay(1000) // TODO("예외 처리 리팩토링 필요")
                        requireActivity().finish()
                    }
                }

                launch {
                    viewModel.gameCode.collectLatest {
                        viewModel.fetchUserRankingList()
                    }
                }

                launch {
                    viewModel.userRankingList.collectLatest {
                        (binding.rvRanking.adapter as UserRankingAdapter).submitList(it)
                    }
                }
            }
        }
    }
}
