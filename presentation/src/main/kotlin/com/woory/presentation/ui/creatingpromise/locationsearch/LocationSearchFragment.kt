package com.woory.presentation.ui.creatingpromise.locationsearch

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
import com.woory.presentation.BuildConfig
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentLocationSearchBinding
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.ui.creatingpromise.CreatingPromiseViewModel
import com.woory.presentation.util.REQUIRE_PERMISSION_TEXT
import com.woory.presentation.util.animRightToLeftNavOption
import com.woory.presentation.util.getActivityContext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationSearchFragment :
    BaseFragment<FragmentLocationSearchBinding>(R.layout.fragment_location_search) {

    private val activityViewModel: CreatingPromiseViewModel by activityViewModels()
    private val fragmentViewModel: LocationSearchViewModel by viewModels()

    private val locationManager by lazy {
        requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private lateinit var mapView: TMapView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionResults ->
        val isGranted = permissionResults.values.reduce { acc, b -> acc && b }
        if (isGranted) {
            setCurrentLocation()
        } else {
            Toast.makeText(
                requireContext(),
                REQUIRE_PERMISSION_TEXT,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMapView()
        setUpButton()
        binding.vm = fragmentViewModel
        binding.activityVm = activityViewModel

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                        showSnackBar(it.message ?: DEFAULT_TEXT)
                    }
                }
            }
        }
    }

    private fun setUpMapView() {
        mapView = TMapView(getActivityContext(requireContext())).apply {
            setSKTMapApiKey(BuildConfig.MAP_API_KEY)
            setOnMapReadyListener {
                zoomLevel = DEFAULT_ZOOM_LEVEL
                fragmentViewModel.setIsMapReady(true)

                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )

                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        activityViewModel.choosedLocation.collectLatest {
                            if (it != null) {

                                val latitude = it.latitude
                                val longitude = it.longitude

                                mapView.apply {
                                    setCenterPoint(latitude, longitude)
                                }
                            }
                        }
                    }
                }
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
                    centerPoint: TMapPoint?,
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
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            if (activityViewModel.choosedLocation.value == null) {
                activityViewModel.setChoosedLocation(
                    GeoPoint(it.latitude, it.longitude)
                )
            }
        }
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewModel.setIsMapReady(false)
        activityViewModel.setLocationName("")
    }

    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 15
        private const val DEFAULT_TEXT = ""
    }
}