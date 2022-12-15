package com.woory.almostthere.presentation.ui.gaming

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
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
import com.woory.almostthere.presentation.BuildConfig
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.binding.bindImage
import com.woory.almostthere.presentation.databinding.CustomviewCharacterMarkerBinding
import com.woory.almostthere.presentation.databinding.FragmentGamingBinding
import com.woory.almostthere.presentation.model.GeoPoint
import com.woory.almostthere.presentation.model.MagneticInfo
import com.woory.almostthere.presentation.model.User
import com.woory.almostthere.presentation.model.UserLocation
import com.woory.almostthere.presentation.model.UserProfileImage
import com.woory.almostthere.presentation.model.WooryTMapCircle
import com.woory.almostthere.presentation.ui.BaseFragment
import com.woory.almostthere.presentation.util.DistanceUtil.getDistance
import com.woory.almostthere.presentation.util.TAG
import com.woory.almostthere.presentation.util.TimeConverter.asOffsetDateTime
import com.woory.almostthere.presentation.util.TimeUtils
import com.woory.almostthere.presentation.util.festive
import com.woory.almostthere.presentation.util.getActivityContext
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
        } else {
            setDefaultLocation()
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
        setDetectIsArrived()
    }

    private fun setUpMapView() {
        mapView = TMapView(getActivityContext(requireContext())).apply {
            setSKTMapApiKey(BuildConfig.MAP_API_KEY)
            setOnMapReadyListener {
                setVisibleLogo(false)

                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        fetchPromiseModel()

                        checkCenterLocation {
                            val latitude = it.latitude
                            val longitude = it.longitude
                            setCenterPoint(latitude, longitude)
                        }

                        fetchAllUserInfo { user, userLocation ->
                            viewModel.fetchGameRanking()
                            if (markerMap[user.userId] == null) {
                                makeNewUserMarker(user, userLocation.geoPoint)
                            } else {
                                updateUserMarker(user, userLocation.geoPoint)
                                removeTMapMarkerItem(user.userId)
                            }
                            addTMapMarkerItem(markerMap[user.userId])
                            checkIsArrived(userLocation)
                        }

                        fetchMagneticInfo {
                            removeAllTMapCircle()
                            addTMapCircle(
                                WooryTMapCircle(
                                    it.centerPoint.latitude,
                                    it.centerPoint.longitude,
                                    it.radius
                                )
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

                            setRemainTime()
                        }

                        checkLocationCenterToMe { location ->
                            setCenterPoint(
                                location.latitude,
                                location.longitude
                            )
                            mapView.zoomLevel = DEFAULT_ZOOM_LEVEL
                        }

                        fetchRanking()
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
                    ) = Unit
                })
                binding.layoutPromiseInfo.setOnClickListener {
                    showPromiseInfo()
                }
            }
        }

        binding.containerMap.addView(mapView)
    }

    private fun updateUserMarker(user: User, userLocation: GeoPoint) {
        markerMap[user.userId]?.tMapPoint =
            TMapPoint(
                userLocation.latitude,
                userLocation.longitude
            )
    }

    private fun makeNewUserMarker(user: User, userLocation: GeoPoint) {
        markerMap[user.userId] =
            TMapMarkerItem().apply {
                id = user.userId
                tMapPoint = TMapPoint(
                    userLocation.latitude,
                    userLocation.longitude
                )
                icon = getUserMarker(user.data.profileImage)
            }
    }

    private fun fetchPromiseModel() {
        lifecycleScope.launch {
            viewModel.promiseModel.collectLatest {
                if (it != null) {
                    viewModel.fetchUserList()
                    viewModel.fetchMagneticField(it)
                    viewModel.fetchUserArrival()
                    viewModel.fetchPromiseEnding()
                }
            }
        }
    }

    private fun checkCenterLocation(
        locationCallback: (GeoPoint) -> Unit
    ) {
        lifecycleScope.launch {
            viewModel.centerLocation.collectLatest {
                if (it != null) {
                    locationCallback(it)
                }
            }
        }
    }

    private fun fetchAllUserInfo(
        userLocationCallback: (User, UserLocation) -> Unit
    ) {
        lifecycleScope.launch {
            viewModel.allUsers.collectLatest {
                it?.forEach { user ->
                    fetchUserInfo(user, userLocationCallback)
                }
            }
        }
    }

    private fun fetchUserInfo(
        user: User,
        userLocationCallback: (User, UserLocation) -> Unit
    ) {
        lifecycleScope.launch {
            viewModel.fetchUserLocation(user)
            viewModel.fetchUserHp(user.userId)
            requireNotNull(viewModel.userLocationMap[user.userId]).collect { userLocation ->
                if (userLocation != null) {
                    userLocationCallback(user, userLocation)
                }
            }
        }
    }

    private fun setRemainTime() {
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

    private fun fetchMagneticInfo(
        callback: (MagneticInfo) -> Unit
    ) {
        lifecycleScope.launch {
            viewModel.magneticInfo.collectLatest {
                if (it != null) {
                    callback(it)
                }
            }
        }
    }

    private fun fetchRanking() {
        lifecycleScope.launch {
            viewModel.ranking.collectLatest {
                if (it.isNotEmpty()) {
                    rankingAdapter.submitList(it.chunked(3)[0])
                }
            }
        }
    }

    private fun checkLocationCenterToMe(
        callback: (GeoPoint) -> Unit
    ) {
        lifecycleScope.launch {
            viewModel.centerLocationToMe.collectLatest {
                val myToken = viewModel.userId.value
                if (myToken != null) {
                    val location = viewModel.getUserLocation(myToken)
                    if (location != null) {
                        callback(location.geoPoint)
                    }
                }
            }
        }
    }

    private fun checkIsArrived(userLocation: UserLocation?) {
        val centerPoint = viewModel.magneticInfo.value?.centerPoint
        if (userLocation?.token == viewModel.myUserInfo.userID) {
            if (centerPoint != null && viewModel.isArrived.value.not()) {
                alertShakeDialog(
                    userLocation.geoPoint,
                    centerPoint
                )
            }

        }
    }

    private fun setDetectIsArrived() {
        lifecycleScope.launch {
            viewModel.isArrived.collectLatest { isArrived ->
                if (isArrived) {
                    binding.konfetti.start(festive())
                }
            }
        }
    }

    private fun showPromiseInfo() {
        dismissBottomSheet()
        promiseInfoBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun showBottomSheet(id: String) {
        profileBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.layoutBottomSheet.tvExpectedTime.text = getString(R.string.loading_text)

        val image = viewModel.getUserImage(id) ?: defaultProfileImage
        binding.layoutBottomSheet.layoutCharacterImg.profileImage = image
        binding.layoutBottomSheet.id = id
        binding.layoutBottomSheet.rank = viewModel.getUserRanking(id)

        lifecycleScope.launch {
            binding.layoutBottomSheet.tvLocation.text = viewModel.getAddress(id)

            val remainTime = viewModel.getRemainTime(id) ?: -1
            binding.layoutBottomSheet.tvExpectedTime.text =
                TimeUtils.getStringInMinuteToDay(requireContext(), remainTime)
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
                if (it.isSuccessful && viewModel.centerLocation.value == null && it.result != null) {
                    viewModel.setCenterLocation(GeoPoint(it.result.latitude, it.result.longitude))
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

        if (distance < ARRIVE_STANDARD_LENGTH) {
            shakeDialog.show(
                parentFragmentManager.beginTransaction().remove(shakeDialog),
                ShakeDeviceFragment.TAG
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

    override fun onResume() {
        super.onResume()
        viewModel.getMyLocation()
    }

    companion object {
        private const val ARRIVE_STANDARD_LENGTH = 20
        private const val PROMISE_LOCATION_MARKER_ID = "PromiseLocation"
        private const val DEFAULT_LATITUDE = 37.3588602423595
        private const val DEFAULT_LONGITUDE = 127.105206334597
        private const val DEFAULT_ZOOM_LEVEL = 15
    }
}