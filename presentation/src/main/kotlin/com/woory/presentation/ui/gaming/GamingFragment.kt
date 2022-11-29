package com.woory.presentation.ui.gaming

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.TMapView.OnClickListenerCallback
import com.skt.tmap.overlay.TMapCircle
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
import com.woory.presentation.BuildConfig
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentGamingBinding
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.getActivityContext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamingFragment : BaseFragment<FragmentGamingBinding>(R.layout.fragment_gaming) {

    private lateinit var mapView: TMapView

    private val behavior by lazy {
        BottomSheetBehavior.from(binding.layoutBottomSheet.layoutBottomSheet)
    }

    private val viewModel: GamingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMapView()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.errorState.collectLatest {
                        makeSnackBar(it.message ?: resources.getString(R.string.unknown_error))
                    }
                }
            }
        }

        viewModel.fetchPromiseData()
    }

    // Todo :: 이하 코드 리펙토링 필요
    private fun setUpMapView() {
        mapView = TMapView(getActivityContext(requireContext())).apply {
            setSKTMapApiKey(BuildConfig.MAP_API_KEY)
            setOnMapReadyListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        launch {
                            viewModel.userLocation.collectLatest {
                                if (it != null) {
                                    viewModel.setUserMarker(it)
                                    setCenterPoint(it.geoPoint.latitude, it.geoPoint.longitude)
                                    removeTMapMarkerItem(it.token)
                                    addTMapMarkerItem(viewModel.getUserMarker(it.token))
                                }
                            }
                        }
                        launch {
                            viewModel.magneticInfo.collectLatest {
                                if (it != null) {
                                    removeAllTMapCircle()
                                    addTMapCircle(
                                        TMapCircle(
                                            MAGNETIC_CIRCLE_KEY,
                                            it.centerPoint.latitude,
                                            it.centerPoint.longitude
                                        ).apply {
                                            radius = it.radius * 1000
                                            circleWidth = 2f
                                            areaAlpha = 0
                                            lineColor = Color.BLUE
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            setOnClickListenerCallback(object : OnClickListenerCallback {
                override fun onPressDown(
                    p0: ArrayList<TMapMarkerItem>?,
                    p1: ArrayList<TMapPOIItem>?,
                    p2: TMapPoint?,
                    p3: PointF?
                ) {
                    if (p0?.isNotEmpty() == true) {
                        showBottomSheet()
                    } else {
                        dismissBottomSheet()
                    }
                }

                override fun onPressUp(
                    p0: ArrayList<TMapMarkerItem>?,
                    p1: ArrayList<TMapPOIItem>?,
                    p2: TMapPoint?,
                    p3: PointF?
                ) {
                }
            })
        }

        binding.containerMap.addView(mapView)
    }

    private fun showBottomSheet() {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun dismissBottomSheet() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun makeSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val MAGNETIC_CIRCLE_KEY = "Magnetic"
    }
}