package com.woory.presentation.ui.gaming

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.UserProfileImage
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.DistanceUtil.getDistance
import com.woory.presentation.util.TAG
import com.woory.presentation.util.getActivityContext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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

    private val shakeDialog = ShakeDeviceFragment()

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

        binding.vm = viewModel

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
                                            viewModel.setUserMarker(userLocation)
                                            removeTMapMarkerItem(id)
                                            addTMapMarkerItem(viewModel.getUserMarker(id))

                                            viewModel.isArrived.collectLatest() { isArrived ->
                                                if (isArrived) return@collectLatest
                                                if (userLocation.token == viewModel.myUserInfo.userID) {
                                                    viewModel.magneticInfo.collectLatest { magneticInfo ->
                                                        magneticInfo ?: throw IllegalArgumentException("is MagneticInfo null")
                                                        alertShakeDialog(userLocation.geoPoint, magneticInfo.centerPoint)
                                                    }
                                                }
                                            }
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

                        launch {
                            viewModel.centerLocationToMe.collectLatest {
                                Timber.tag("123123").d("enter")
                                val myToken = viewModel.userId.value ?: return@collectLatest
                                Timber.tag("123123").d(myToken)
                                val location =
                                    viewModel.getUserLocation(myToken) ?: return@collectLatest
                                Timber.tag("123123").d(location.toString())
                                setCenterPoint(
                                    location.geoPoint.latitude,
                                    location.geoPoint.longitude
                                )
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

    private fun alertShakeDialog(userLocation: GeoPoint, dest: GeoPoint) {
        val distance = getDistance(
            userLocation,
            dest
        )

        if (distance < ARRIVE_STANDARD_LENGTH && !shakeDialog.isVisible) {
            shakeDialog.show(
                parentFragmentManager,
                shakeDialog.TAG
            )
        } else {
            if (shakeDialog.isVisible) {
                shakeDialog.dismiss()
            }
        }
    }

    companion object {
        private const val MAGNETIC_CIRCLE_KEY = "Magnetic"
        private const val ARRIVE_STANDARD_LENGTH = 20
    }
}