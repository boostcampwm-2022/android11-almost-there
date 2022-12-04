package com.woory.presentation.ui.gaming

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
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
import com.skt.tmap.overlay.TMapMarkerItem2
import com.skt.tmap.poi.TMapPOIItem
import com.woory.presentation.BuildConfig
import com.woory.presentation.R
import com.woory.presentation.databinding.CustomviewCharaterMarkerBinding
import com.woory.presentation.databinding.FragmentGamingBinding
import com.woory.presentation.model.UserProfileImage
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

    private val defaultProfileImage by lazy {
        UserProfileImage("#000000", 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.fetchPromiseData()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.errorState.collectLatest {
                        makeSnackBar(it.message ?: resources.getString(R.string.unknown_error))
                    }
                }
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutBottomSheet.layoutCharacterImg.profileImage = defaultProfileImage
        binding.layoutBottomSheet.vm = viewModel
        binding.layoutBottomSheet.lifecycleOwner = viewLifecycleOwner

        dismissBottomSheet()

        setUpMapView()
    }

    private fun setUpMapView() {
        mapView = TMapView(getActivityContext(requireContext())).apply {
            setSKTMapApiKey(BuildConfig.MAP_API_KEY)
            setOnMapReadyListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        launch {
                            viewModel.allUsers.collectLatest {
                                it?.forEach { id ->
                                    viewModel.userLocationMap[id]?.collectLatest { userLocation ->
                                        if (userLocation != null) {
                                            val marker = TMapMarkerItem2(id).apply {
                                                tMapPoint = TMapPoint(userLocation.geoPoint.latitude, userLocation.geoPoint.longitude)
                                                iconList = viewModel.getUserImage(id)?.let { userProfileImage ->
                                                    arrayListOf(getUserMarker(userProfileImage))
                                                } ?: arrayListOf()
                                            }
                                            viewModel.setUserMarker(userLocation, marker)
                                            removeTMapMarkerItem(id)
                                            addTMapMarkerItem2Icon(viewModel.getUserMarker(id))
                                        }
                                    }
                                }
                            }
                        }
                        launch {
                            viewModel.magneticInfo.collectLatest {
                                if (it != null) {
                                    removeAllTMapCircle()
                                    setCenterPoint(
                                        it.centerPoint.latitude,
                                        it.centerPoint.longitude
                                    )
                                    addTMapCircle(
                                        TMapCircle(
                                            MAGNETIC_CIRCLE_KEY,
                                            it.centerPoint.latitude,
                                            it.centerPoint.longitude
                                        ).apply {
                                            radius = it.radius
                                            circleWidth = 2f
                                            areaAlpha = 10
                                            areaColor = Color.RED
                                            lineColor = Color.RED
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
                        showBottomSheet(p0[0].id)
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

    private fun showBottomSheet(id: String) {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val image = viewModel.getUserImage(id) ?: defaultProfileImage
        binding.layoutBottomSheet.layoutCharacterImg.profileImage = image
        binding.layoutBottomSheet.id = id
        binding.layoutBottomSheet.rank = viewModel.getUserRanking(id)

        lifecycleScope.launch {
            val address = viewModel.getAddress(id) ?: getString(R.string.unknown_error)
            binding.layoutBottomSheet.tvLocation.text = address
        }
    }

    private fun dismissBottomSheet() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun makeSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun getUserMarker(userProfileImage: UserProfileImage): Bitmap {
        val markerBinding: CustomviewCharaterMarkerBinding = CustomviewCharaterMarkerBinding.inflate(
            layoutInflater, binding.root as ViewGroup?, false
        )
        markerBinding.viewTail.setColorFilter(Color.parseColor(userProfileImage.color))
        markerBinding.containerBody.setColorFilter(Color.parseColor(userProfileImage.color))
        markerBinding.layoutMarker.profileImage = userProfileImage
        markerBinding.lifecycleOwner = this
        val view = markerBinding.root
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        return view.drawToBitmap()
    }

    companion object {
        private const val MAGNETIC_CIRCLE_KEY = "Magnetic"
    }
}