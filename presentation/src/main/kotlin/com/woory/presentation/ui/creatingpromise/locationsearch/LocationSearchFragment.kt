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
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
import com.woory.presentation.BuildConfig
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentLocationSearchBinding
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.REQUIRE_PERMISSION_TEXT
import com.woory.presentation.util.animRightToLeftNavOption
import com.woory.presentation.util.getActivityContext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationSearchFragment :
    BaseFragment<FragmentLocationSearchBinding>(R.layout.fragment_location_search) {

    private val viewModel: CreatingPromiseViewModel by activityViewModels()

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

    private val bitmap by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_destination_flag)?.toBitmap()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMapView()
        setUpButton()
        binding.vm = viewModel
    }

    private fun setUpMapView() {
        mapView = TMapView(getActivityContext(requireContext())).apply {
            setSKTMapApiKey(BuildConfig.MAP_API_KEY)
            setOnMapReadyListener {
                zoomLevel = DEFAULT_ZOOM_LEVEL
                viewModel.setIsMapReady(true)

                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )

                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.choosedLocation.collectLatest {
                            if (it != null) {

                                val latitude = it.latitude
                                val longitude = it.longitude

//                                val marker = TMapMarkerItem().apply {
//                                    id = MARKER_ID
//                                    icon = bitmap
//                                    tMapPoint = TMapPoint(latitude, longitude)
//                                }

                                mapView.apply {
//                                    removeTMapMarkerItem(MARKER_ID)
                                    setCenterPoint(latitude, longitude)
//                                    addTMapMarkerItem(marker)
                                }
                            }
                        }
                    }
//                    repeatOnLifecycle(Lifecycle.State.STARTED) {
//                        viewModel.choosedLocation.collect {
//                            if (it != null) {
//                                setCenterPoint(
//                                    it.latitude,
//                                    it.longitude
//                                )
//                            }
//                        }
//                    }
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
                        viewModel.setChoosedLocation(GeoPoint(it.latitude, it.longitude))
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

        // TODO : 장소 결정 로직 추가
        binding.btnSubmit.root.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.choosedLocation.collectLatest {
//                    if (it != null) {
//                        viewModel.setPromiseLocation(it)
//                    }
                }
            }
            findNavController().popBackStack()
        }
    }


    @SuppressLint("MissingPermission")
    private fun setCurrentLocation() {
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            if (viewModel.choosedLocation.value == null) {
                viewModel.setChoosedLocation(
                    GeoPoint(it.latitude, it.longitude)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setIsMapReady(false)
    }

    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 15
        private const val CURRENT_LOCATION_TEXT = ""
        private const val MARKER_ID = "searchedDestination"
    }
}