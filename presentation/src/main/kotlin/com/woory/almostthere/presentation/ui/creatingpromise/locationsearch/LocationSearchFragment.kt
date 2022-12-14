package com.woory.almostthere.presentation.ui.creatingpromise.locationsearch

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
import com.woory.almostthere.presentation.BuildConfig
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.databinding.FragmentLocationSearchBinding
import com.woory.almostthere.presentation.extension.repeatOnStarted
import com.woory.almostthere.presentation.model.GeoPoint
import com.woory.almostthere.presentation.ui.BaseFragment
import com.woory.almostthere.presentation.ui.creatingpromise.CreatingPromiseViewModel
import com.woory.almostthere.presentation.util.REQUIRE_PERMISSION_TEXT
import com.woory.almostthere.presentation.util.animRightToLeftNavOption
import com.woory.almostthere.presentation.util.getActivityContext
import com.woory.almostthere.presentation.util.getExceptionMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationSearchFragment :
    BaseFragment<FragmentLocationSearchBinding>(R.layout.fragment_location_search) {

    private val activityViewModel: CreatingPromiseViewModel by activityViewModels()
    private val fragmentViewModel: LocationSearchViewModel by viewModels()

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val locationManager by lazy {
        requireContext().getSystemService(LocationManager::class.java)
    }

    private val connectivityManager by lazy {
        requireContext().getSystemService(ConnectivityManager::class.java)
    }

    private val networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                viewLifecycleOwner.lifecycleScope.launch {
                    setUpMapView()
                }
            }
        }
    }

    private lateinit var mapView: TMapView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionResults ->
        val isGranted = permissionResults.values.reduce { acc, b -> acc && b }
        if (isGranted) {
            setCurrentLocation()
        } else {
            showSnackBar(REQUIRE_PERMISSION_TEXT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpButton()
        binding.vm = fragmentViewModel
        binding.activityVm = activityViewModel

        repeatOnStarted {
            launch {
                fragmentViewModel.promiseLocation.collectLatest {
                    if (it != null) {
                        activityViewModel.setPromiseLocation(it)
                        findNavController().popBackStack()
                    }
                }
            }
            launch {
                fragmentViewModel.errorEvent.collectLatest {
                    val message = getExceptionMessage(requireContext(), it)
                    showSnackBar(message)
                }
            }
        }
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun setUpMapView() {
        mapView = TMapView(getActivityContext(requireContext())).apply {
            setSKTMapApiKey(BuildConfig.MAP_API_KEY)
            setOnMapReadyListener {
                setVisibleLogo(false)
                zoomLevel = DEFAULT_ZOOM_LEVEL
                fragmentViewModel.setIsMapReady(true)
                binding.iconCenterLocationMarker.visibility = View.VISIBLE

                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE
                    )
                )

                repeatOnStarted {
                    activityViewModel.choosedLocation.collectLatest {
                        if (it != null) {

                            val latitude = it.latitude
                            val longitude = it.longitude

                            setCenterPoint(latitude, longitude)
                        }
                    }
                }
            }

            setOnLongClickListenerCallback { _, _, tMapPoint ->
                activityViewModel.setChoosedLocation(
                    GeoPoint(
                        tMapPoint.latitude,
                        tMapPoint.longitude
                    )
                )
            }

            setOnClickListenerCallback(object : TMapView.OnClickListenerCallback {
                override fun onPressDown(
                    p0: ArrayList<TMapMarkerItem>?,
                    p1: ArrayList<TMapPOIItem>?,
                    p2: TMapPoint?,
                    p3: PointF?
                ) {
                    binding.iconCenterLocationMarker.alpha = 0.5f
                }

                override fun onPressUp(
                    p0: ArrayList<TMapMarkerItem>?,
                    p1: ArrayList<TMapPOIItem>?,
                    p2: TMapPoint?,
                    p3: PointF?
                ) {
                    centerPoint?.let {
                        binding.iconCenterLocationMarker.alpha = 1f
                        activityViewModel.setChoosedLocation(GeoPoint(it.latitude, it.longitude))
                    }
                }
            })
        }

        binding.containerMapview.addView(mapView)
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

    private fun setUpButton() {
        binding.btnSearchLocation.setOnClickListener {
            findNavController().navigate(
                R.id.nav_location_search_graf,
                null,
                animRightToLeftNavOption
            )
        }

        binding.btnSubmitChoosedLocation.btnSubmit.setOnClickListener {
            fragmentViewModel.findAddressByLocation(activityViewModel.choosedLocation.value)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setCurrentLocation() {
        if (locationManager.isProviderEnabled(LocationManager.FUSED_PROVIDER)) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result != null) {
                        if (activityViewModel.choosedLocation.value == null) {
                            activityViewModel.setChoosedLocation(
                                GeoPoint(it.result.latitude, it.result.longitude)
                            )
                        }
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

    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun setDefaultLocation() {
        activityViewModel.setChoosedLocation(
            GeoPoint(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewModel.setIsMapReady(false)
        activityViewModel.setLocationName("")
        unregisterNetworkCallback()
    }

    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 15
        private const val DEFAULT_LATITUDE = 37.3588602423595
        private const val DEFAULT_LONGITUDE = 127.105206334597
    }
}