package com.woory.almostthere.presentation.ui.gameresult

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.databinding.FragmentGameResultBinding
import com.woory.almostthere.presentation.extension.repeatOnStarted
import com.woory.almostthere.presentation.ui.BaseFragment
import com.woory.almostthere.presentation.ui.customview.topitemresize.TopItemResizeDecoration
import com.woory.almostthere.presentation.ui.customview.topitemresize.TopItemResizeScrollListener
import com.woory.almostthere.presentation.util.festive
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
        playKonfetti()
    }

    private fun setUpToolbar() {
        (requireActivity() as GameResultActivity).supportActionBar?.hide()
    }

    private fun setUpClickListener() {
        binding.layoutButton.btnLeft.setOnClickListener {
            requireActivity().finish()
        }
        binding.layoutButton.btnRight.setOnClickListener {
            viewModel.setShowDialogEvent()
            findNavController().navigate(R.id.nav_calculate_frag)
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

            itemAnimator = null
            addItemDecoration(TopItemResizeDecoration())
            addOnScrollListener(TopItemResizeScrollListener(linearLayoutManager))
        }
    }

    private fun observeData() {
        repeatOnStarted {
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

            launch {
                viewModel.myRankingNumber.collectLatest {
                    binding.tvGameResultMyRanking.text =
                        String.format(
                            getString(R.string.my_ranking),
                            it?.toString() ?: getString(R.string.null_value)
                        )
                }
            }
        }
    }

    private fun playKonfetti() {
        binding.konfetti.start(festive())
    }
}
