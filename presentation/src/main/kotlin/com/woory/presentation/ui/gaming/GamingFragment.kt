package com.woory.presentation.ui.gaming

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PointF
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import com.woory.presentation.binding.bindImage
import com.woory.presentation.databinding.CustomviewCharacterMarkerBinding
import com.woory.presentation.databinding.FragmentGamingBinding
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.UserProfileImage
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.DistanceUtil.getDistance
import com.woory.presentation.util.NO_MAGNETIC_INFO_EXCEPTION
import com.woory.presentation.util.TAG
import com.woory.presentation.util.TimeConverter.asOffsetDateTime
import com.woory.presentation.util.TimeUtils
import com.woory.presentation.util.festive
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

    private val markerImage by lazy {
        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_destination_flag)?.toBitmap()
    }

    private val markerMap = mutableMapOf<String, TMapMarkerItem>()

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val locationManager by lazy {
        requireContext().getSystemService(LocationManager::class.java)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionResults ->
        val isGranted = permissionResults.values.reduce { acc, b -> acc && b }
        if (isGranted) {
            setCurrentLocation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.fetchPromise()

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

        binding.rvRanking.adapter = rankingAdapter

        binding.layoutBottomSheet.layoutCharacterImg.profileImage = defaultProfileImage
        binding.layoutBottomSheet.vm = viewModel
        binding.layoutBottomSheet.lifecycleOwner = viewLifecycleOwner

        binding.layoutBottomSheetPromise.vm = viewModel
        binding.layoutBottomSheetPromise.lifecycleOwner = viewLifecycleOwner
        binding.layoutBottomSheetPromise.pattern = "yyyy:MM:hh a hh:mm"

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE
            )
        )

        dismissBottomSheet()

        setUpMapView()
    }

    private fun setUpMapView() {
        mapView = TMapView(getActivityContext(requireContext())).apply {
            setSKTMapApiKey(BuildConfig.MAP_API_KEY)
            setOnMapReadyListener {
                setVisibleLogo(false)

                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        launch {
                            viewModel.promiseModel.collectLatest {
                                if (it != null) {
                                    viewModel.fetchUserList()
                                    viewModel.fetchMagneticField(it)
                                    viewModel.fetchUserArrival()
                                    viewModel.fetchPromiseEnding()
                                }
                            }
                        }

                        launch {
                            viewModel.centerLocation.collectLatest {
                                if (it != null) {
                                    val latitude = it.latitude
                                    val longitude = it.longitude
                                    setCenterPoint(latitude, longitude)
                                }
                            }
                        }

                        launch {
                            viewModel.allUsers.collectLatest {
                                it?.forEach { user ->
                                    launch {
                                        viewModel.fetchUserLocation(user)
                                        viewModel.fetchUserHp(user.userId)
                                        requireNotNull(viewModel.userLocationMap[user.userId]).collect { userLocation ->
                                            if (userLocation != null) {
                                                viewModel.fetchGameRanking()
                                                if (markerMap[user.userId] == null) {
                                                    markerMap[user.userId] =
                                                        TMapMarkerItem().apply {
                                                            id = user.userId
                                                            tMapPoint = TMapPoint(
                                                                userLocation.geoPoint.latitude,
                                                                userLocation.geoPoint.longitude
                                                            )
                                                            icon =
                                                                getUserMarker(user.data.profileImage)
                                                        }
                                                    addTMapMarkerItem(markerMap[user.userId])
                                                } else {
                                                    markerMap[user.userId]?.tMapPoint =
                                                        TMapPoint(
                                                            userLocation.geoPoint.latitude,
                                                            userLocation.geoPoint.longitude
                                                        )
                                                    removeTMapMarkerItem(user.userId)
                                                    addTMapMarkerItem(markerMap[user.userId])
                                                }
                                            }

                                            launch {
                                                viewModel.isArrived.collectLatest { isArrived ->
                                                    if (isArrived) {
                                                        binding.konfetti.start(festive())
                                                        return@collectLatest
                                                    }
                                                    if (userLocation?.token == viewModel.myUserInfo.userID) {
                                                        viewModel.magneticInfo.collectLatest { magneticInfo ->
                                                            if (magneticInfo != null) {
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

                                    removeTMapMarkerItem(PROMISE_LOCATION_MARKER_ID)
                                    addTMapMarkerItem(TMapMarkerItem().apply {
                                        id = PROMISE_LOCATION_MARKER_ID
                                        icon = markerImage
                                        tMapPoint = TMapPoint(
                                            it.centerPoint.latitude,
                                            it.centerPoint.longitude
                                        )
                                    }
                                    )

                                    viewModel.promiseModel.value?.let { promise ->
                                        binding.tvTime.text =
                                            TimeUtils.getDurationStringInMinuteToDay(
                                                requireContext(),
                                                System.currentTimeMillis()
                                                    .asOffsetDateTime(),
                                                promise.data.promiseDateTime
                                            )
                                    }
                                }
                            }
                        }

                        launch {
                            viewModel.centerLocationToMe.collectLatest {
                                val myToken = viewModel.userId.value
                                if (myToken != null) {
                                    val location = viewModel.getUserLocation(myToken)
                                    if (location != null) {
                                        setCenterPoint(
                                            location.geoPoint.latitude,
                                            location.geoPoint.longitude
                                        )
                                        mapView.zoomLevel = 100
                                    }
                                }
                            }
                        }

                        launch {
                            viewModel.ranking.collectLatest {
                                if (it.isNotEmpty()) {
                                    rankingAdapter.submitList(it.chunked(3)[0])
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
                        if (p0?.isNotEmpty() == true && p0[0].id != PROMISE_LOCATION_MARKER_ID) {
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

    private fun showPromiseInfo() {
        dismissBottomSheet()
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
                    val remainTime = viewModel.getRemainTime(id) ?: -1
                    binding.layoutBottomSheet.tvExpectedTime.text =
                        TimeUtils.getStringInMinuteToDay(requireContext(), remainTime)
                }
            }
        }
    }

    private fun dismissBottomSheet() {
        profileBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        promiseInfoBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.layoutBottomSheet.tvExpectedTime.text = getString(R.string.loading_text)
    }

    private fun makeSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun getUserMarker(userProfileImage: UserProfileImage): Bitmap {
        val markerBinding: CustomviewCharacterMarkerBinding =
            CustomviewCharacterMarkerBinding.inflate(
                layoutInflater, binding.root as ViewGroup?, false
            )

        markerBinding.ivCharacter.bindImage(userProfileImage.imageIndex)
        markerBinding.ivCharacter.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(userProfileImage.color))
        markerBinding.lifecycleOwner = this

        val view = markerBinding.root
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        return view.drawToBitmap()
    }

    private fun requestPermissions(permissions: Array<String>) {
        val permissionResult = permissions
            .map {
                ActivityCompat.checkSelfPermission(requireContext(), it) ==
                        PackageManager.PERMISSION_GRANTED
            }
            .reduce { a, b -> a && b }

        if (permissionResult) setCurrentLocation()
        else requestPermissionLauncher.launch(permissions)
    }

    @SuppressLint("MissingPermission")
    private fun setCurrentLocation() {
        if (locationManager.isProviderEnabled(LocationManager.FUSED_PROVIDER)) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                if (it.isSuccessful) {
                    if (viewModel.centerLocation.value == null) {
                        viewModel.setCenterLocation(
                            GeoPoint(it.result.latitude, it.result.longitude)
                        )
                    }
                } else {
                    setDefaultLocation()
                }
            }.addOnFailureListener {
                setDefaultLocation()
            }
        } else {
            setDefaultLocation()
        }
    }

    private fun alertShakeDialog(userLocation: GeoPoint, dest: GeoPoint) {
        val distance = getDistance(
            userLocation,
            dest
        )

        if (distance < ARRIVE_STANDARD_LENGTH && !shakeDialog.isAdded) {
            shakeDialog.show(
                parentFragmentManager,
                shakeDialog.TAG
            )
        }
    }

    private fun setDefaultLocation() {
        viewModel.setCenterLocation(
            GeoPoint(
                DEFAULT_LATITUDE,
                DEFAULT_LONGITUDE
            )
        )
    }

    companion object {
        private const val MAGNETIC_CIRCLE_KEY = "Magnetic"
        private const val ARRIVE_STANDARD_LENGTH = 20
        private const val PROMISE_LOCATION_MARKER_ID = "PromiseLocation"
        private const val DEFAULT_LATITUDE = 37.3588602423595
        private const val DEFAULT_LONGITUDE = 127.105206334597
    }
}