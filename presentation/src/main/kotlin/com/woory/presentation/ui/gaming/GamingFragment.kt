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
import com.skt.tmap.poi.TMapPOIItem
import com.woory.presentation.BuildConfig
import com.woory.presentation.R
import com.woory.presentation.databinding.CustomviewCharaterMarkerBinding
import com.woory.presentation.databinding.FragmentGamingBinding
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.UserProfileImage
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.DistanceUtil.getDistance
import com.woory.presentation.util.NO_MAGNETIC_INFO_EXCEPTION
import com.woory.presentation.util.TAG
import com.woory.presentation.util.TimeConverter.asOffsetDateTime
import com.woory.presentation.util.TimeUtils
import com.woory.presentation.util.getActivityContext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamingFragment : BaseFragment<FragmentGamingBinding>(R.layout.fragment_gaming) {

    private lateinit var mapView: TMapView

    private val profileBehavior by lazy {
        BottomSheetBehavior.from(binding.layoutBottomSheet.layoutBottomSheet)
    }

    private val promiseInfoBehavior by lazy {
        BottomSheetBehavior.from(binding.layoutBottomSheetPromise.layoutBottomSheet)
    }

    private val viewModel: GamingViewModel by activityViewModels()

    private val defaultProfileImage by lazy {
        UserProfileImage("#000000", 0)
    }

    private val shakeDialog = ShakeDeviceFragment()

    private val rankingAdapter by lazy {
        GamingRankingAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.fetchPromiseData()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorState.collectLatest {
                    makeSnackBar(it.message ?: resources.getString(R.string.unknown_error))
                }
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        binding.rvRanking.adapter = rankingAdapter

        binding.layoutBottomSheet.layoutCharacterImg.profileImage = defaultProfileImage
        binding.layoutBottomSheet.vm = viewModel
        binding.layoutBottomSheet.lifecycleOwner = viewLifecycleOwner

        binding.layoutBottomSheetPromise.vm = viewModel
        binding.layoutBottomSheetPromise.lifecycleOwner = viewLifecycleOwner
        binding.layoutBottomSheetPromise.pattern = "yyyy:MM:hh hh:mm"

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
                                            launch {
                                                val marker = TMapMarkerItem().apply {
                                                    this.id = id
                                                    tMapPoint = TMapPoint(
                                                        userLocation.geoPoint.latitude,
                                                        userLocation.geoPoint.longitude
                                                    )
                                                    icon = viewModel.getUserImage(id)
                                                        ?.let { userProfileImage ->
                                                            getUserMarker(userProfileImage)
                                                        }
                                                }
                                                viewModel.setUserMarker(userLocation, marker)
                                                removeTMapMarkerItem(id)
                                                addTMapMarkerItem(viewModel.getUserMarker(id))
                                            }

                                            launch {
                                                viewModel.isArrived.collectLatest { isArrived ->
                                                    if (isArrived) return@collectLatest
                                                    if (userLocation.token == viewModel.myUserInfo.userID) {
                                                        viewModel.magneticInfo.collectLatest { magneticInfo ->
                                                            magneticInfo
                                                                ?: throw NO_MAGNETIC_INFO_EXCEPTION
                                                            alertShakeDialog(
                                                                userLocation.geoPoint,
                                                                magneticInfo.centerPoint
                                                            )
                                                        }
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

                                    viewModel.promiseModel.value?.let { promise ->
                                        binding.tvTime.text =
                                            TimeUtils.getDurationStringInMinuteToDay(
                                                requireContext(),
                                                System.currentTimeMillis().asOffsetDateTime(),
                                                promise.data.promiseDateTime
                                            )
                                    }
                                }
                            }
                        }

                        launch {
                            viewModel.centerLocationToMe.collectLatest {
                                val myToken = viewModel.userId.value ?: return@collectLatest
                                val location =
                                    viewModel.getUserLocation(myToken) ?: return@collectLatest
                                setCenterPoint(
                                    location.geoPoint.latitude,
                                    location.geoPoint.longitude
                                )
                                mapView.zoomLevel = 100
                            }
                        }

                        launch {
                            viewModel.ranking.collectLatest {
                                rankingAdapter.submitList(it.chunked(3)[0])
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
                binding.layoutPromiseInfo.setOnClickListener {
                    showPromiseInfo()
                }
            }
        }

        binding.containerMap.addView(mapView)
    }

    private fun showPromiseInfo(){
        promiseInfoBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun showBottomSheet(id: String) {
        profileBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        val image = viewModel.getUserImage(id) ?: defaultProfileImage
        binding.layoutBottomSheet.layoutCharacterImg.profileImage = image
        binding.layoutBottomSheet.id = id
        binding.layoutBottomSheet.rank = viewModel.getUserRanking(id)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    val address = viewModel.getAddress(id) ?: getString(R.string.unknown_error)
                    binding.layoutBottomSheet.tvLocation.text = address
                }
                launch {
                    val remainTime = viewModel.getRemainTime() ?: -1
                    binding.layoutBottomSheet.tvExpectedTime.text =
                        TimeUtils.getStringInMinuteToDay(requireContext(), remainTime)
                }
            }
        }
    }

    private fun dismissBottomSheet() {
        profileBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        promiseInfoBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun makeSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun getUserMarker(userProfileImage: UserProfileImage): Bitmap {
        val markerBinding: CustomviewCharaterMarkerBinding =
            CustomviewCharaterMarkerBinding.inflate(
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