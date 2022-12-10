package com.woory.presentation.ui.promiseinfo

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.woory.presentation.BuildConfig
import com.woory.presentation.R
import com.woory.presentation.background.alarm.AlarmFunctions
import com.woory.presentation.databinding.FragmentPromiseInfoBinding
import com.woory.presentation.model.AlarmState
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.ReadyUser
import com.woory.presentation.model.mapper.alarm.asUiModel
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.getActivityContext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromiseInfoFragment :
    BaseFragment<FragmentPromiseInfoBinding>(R.layout.fragment_promise_info) {

    private val viewModel: PromiseInfoViewModel by activityViewModels()
    private lateinit var mapView: TMapView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val participantAdapter by lazy {
        PromiseUserAdapter(viewModel)
    }

    private val clipBoard by lazy {
        requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }

    private val markerImage by lazy {
        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_destination_flag)?.toBitmap()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        setUpMapView()
        setUpButtonListener()
        setUsers()

        viewModel.fetchPromiseDate()
        viewModel.fetchReadyUsers()

        binding.apply {
            vm = viewModel
            defaultString = ""
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.errorState.collect {
                        val errorMessage =
                            it.message ?: requireContext().resources.getString(R.string.unknown_error)
                        makeSnackBar(
                            "Error : $errorMessage"
                        )
                    }
                }

                launch {
                    setAlarm()
                }

                launch {
                    setReadyButton()
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

        binding.btnCodeShare.setOnClickListener {
//            shareCode(viewModel.gameCode.value)
        }

        binding.btnReady.btnSubmit.setOnClickListener {
            readyGame()
        }
    }

    private fun setUpMapView() {
        mapView = TMapView(getActivityContext(requireContext())).apply {
            setSKTMapApiKey(BuildConfig.MAP_API_KEY)

            setOnMapReadyListener {
                binding.rvPromiseParticipant.adapter = participantAdapter
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.promiseModel.collect { promise ->
                            if (promise != null) {
                                setMapItem(
                                    this@apply,
                                    promise.data.promiseLocation.geoPoint.latitude,
                                    promise.data.promiseLocation.geoPoint.longitude
                                )
                            }
                        }
                    }
                }
            }
        }
        binding.containerMap.addView(mapView)
    }

    private fun setUsers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.users.collectLatest { users ->
                    viewModel.readyUsers.collectLatest { readyUsers ->
                        participantAdapter.submitList(users.map { user ->
                            val isReady = user.userId in readyUsers
                            ReadyUser(isReady, user)
                        })
                    }
                }
            }
        }
    }

    private fun setMapItem(tMapView: TMapView, latitude: Double, longitude: Double) {
        tMapView.apply {
            setCenterPoint(latitude, longitude)
            zoomLevel = 15

            val marker = TMapMarkerItem().apply {
                id = PROMISE_LOCATION_MARKER_ID
                icon = markerImage
                tMapPoint = TMapPoint(latitude, longitude)
            }

            removeAllTMapMarkerItem()
            addTMapMarkerItem(marker)
        }
    }

    private fun readyGame() {
        if (viewModel.blockReady) {
            makeSnackBar(getString(R.string.btn_ready_doing))
            return
        }

        getLastLocation { startGeoPoint ->
            viewModel.setUserCurrentLocation(startGeoPoint)
            viewModel.setPromiseMagneticRadius(startGeoPoint)
        }
    }

    private fun getLastLocation(callback: (GeoPoint) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            makeSnackBar(getString(R.string.location_permission_error))
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location == null) {
                makeSnackBar(getString(R.string.location_off_error))
                return@addOnSuccessListener
            }

            val geoPoint = GeoPoint(location.latitude, location.longitude)
            callback(geoPoint)
        }
    }

    private suspend fun setReadyButton() {
        viewModel.isUserReady.collectLatest { readyStatus ->
            when (readyStatus) {
                ReadyStatus.NOT -> {
                    binding.btnReady.buttonText = getString(R.string.btn_ready_not)
                    binding.btnReady.btnSubmit.isEnabled = true
                }
                ReadyStatus.READY -> {
                    binding.btnReady.buttonText = getString(R.string.btn_ready_done)
                    binding.btnReady.btnSubmit.isEnabled = false
                }
                ReadyStatus.BEFORE -> {
                    binding.btnReady.buttonText = getString(R.string.btn_ready_before)
                    binding.btnReady.btnSubmit.isEnabled = false
                }
                ReadyStatus.AFTER -> {
                    binding.btnReady.buttonText = getString(R.string.btn_ready_after)
                    binding.btnReady.btnSubmit.isEnabled = false
                }
            }
        }
    }

    private suspend fun setAlarm() {
        viewModel.isAvailSetAlarm.collectLatest { isAvailable ->
            if (isAvailable) {
                val alarmFunctions = AlarmFunctions(requireContext())
                val promiseCode = viewModel.promiseModel.value?.code ?: return@collectLatest

                viewModel.getPromiseAlarmByCode(promiseCode).onSuccess { promiseAlarm ->
                    alarmFunctions.registerAlarm(promiseAlarm.asUiModel().copy(state = AlarmState.START))
                }
            }
        }
    }

    // TODO : 카카오톡 공유 코드
//    private fun shareCode(code: String) {
//        val defaultFeed = FeedTemplate(
//            content = Content(
//                title = "오늘 먹고싶은 것",
//                description = "오늘은 달달한게 땡긴다",
//                imageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
//                link = Link(
//                    mobileWebUrl = "https://woory-almost-there.com/main"
//                )
//            ),
//            buttons = listOf(
//                Button(
//                    "앱으로 보기",
//                    Link(
//                        androidExecutionParams = mapOf("key1" to "value1"),
//                    )
//                )
//            )
//        )
//
//        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
//
//            ShareClient.instance.shareDefault(
//                requireContext(),
//                defaultFeed
//            ) { sharingResult, error ->
//                if (error != null) {
//
//                } else if (sharingResult != null) {
//                    startActivity(sharingResult.intent)
//                }
//            }
//        } else {
//            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)
//            try {
//                KakaoCustomTabsClient.openWithDefault(requireContext(), sharerUrl)
//            } catch (e: UnsupportedOperationException) {
//            }
//
//            try {
//                KakaoCustomTabsClient.open(requireContext(), sharerUrl)
//            } catch (e: ActivityNotFoundException) {
//            }
//        }
//    }

    private fun makeSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val PROMISE_LOCATION_MARKER_ID = "promiseLocation"
        private const val TAG = "123123"
    }
}