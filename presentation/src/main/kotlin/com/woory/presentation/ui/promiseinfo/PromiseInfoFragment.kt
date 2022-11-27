package com.woory.presentation.ui.promiseinfo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentPromiseInfoBinding
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.MAP_API_KEY
import com.woory.presentation.util.getActivityContext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromiseInfoFragment :
    BaseFragment<FragmentPromiseInfoBinding>(R.layout.fragment_promise_info) {

    private val viewModel: PromiseInfoViewModel by activityViewModels()
    private lateinit var mapView: TMapView
    private val participantAdapter by lazy {
        PromiseUserAdapter(viewModel)
    }

    private val clipBoard by lazy {
        requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }

    private val markerImage by lazy {
        ContextCompat.getDrawable(requireActivity(), R.drawable.bg_speech_bubble)?.toBitmap()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpMapView()
        setUpButtonListener()

        viewModel.fetchPromiseDate()
        binding.apply {
            vm = viewModel
            defaultString = ""
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it is PromiseUiState.Fail) {
                        makeSnackBar(it.message)
                        viewModel.setUiState(PromiseUiState.Waiting)
                    }
                }
            }
        }
    }

    private fun setUpButtonListener() {
        binding.btnCodeCopy.setOnClickListener {
            val clip = ClipData.newPlainText("", viewModel.gameCode.value)
            clipBoard.setPrimaryClip(clip)
            makeSnackBar(getString(R.string.copy_complete))
        }
    }

    private fun setUpMapView() {
        mapView = TMapView(getActivityContext(requireContext())).apply {
            setSKTMapApiKey(MAP_API_KEY)

            setOnMapReadyListener {
                binding.rvPromiseParticipant.adapter = participantAdapter
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.promiseModel.collect {
                            if (it != null) {
                                setMapItem(
                                    this@apply,
                                    it.data.promiseLocation.geoPoint.latitude,
                                    it.data.promiseLocation.geoPoint.longitude
                                )
                            }
                            participantAdapter.submitList(it?.data?.users)
                        }
                    }
                }
            }
        }
        binding.containerMap.addView(mapView)
    }

    private fun setMapItem(tMapView: TMapView, latitude: Double, longitude: Double) {
        tMapView.apply {
            setCenterPoint(latitude, longitude)
            zoomLevel = 15

            val marker = TMapMarkerItem().apply {
                id = "promiseLocation"
                icon = markerImage
                tMapPoint = TMapPoint(latitude, longitude)
            }

            removeAllTMapMarkerItem()
            addTMapMarkerItem(marker)
        }
    }

    private fun makeSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}