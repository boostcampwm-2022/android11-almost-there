package com.woory.presentation.ui.creatingpromise

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.skt.tmap.TMapView
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentLocationSearchBinding
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.Location
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.MAP_API_KEY
import com.woory.presentation.util.REQUIRE_PERMISSION_TEXT
import com.woory.presentation.util.getActivityContext
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMapView()
        setUpButton()
    }

    private fun setUpMapView() {
        mapView = TMapView(getActivityContext(requireContext())).apply {
            setSKTMapApiKey(MAP_API_KEY)
            setOnMapReadyListener {
                zoomLevel = DEFAULT_ZOOM_LEVEL

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    setCurrentLocation()
                } else {
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.choosedLocation.collect {
                            if (it != null) {
                                setCenterPoint(
                                    it.geoPoint.latitude,
                                    it.geoPoint.longitude
                                )
                            }
                        }
                    }
                }
            }
        }

        binding.containerMapview.addView(mapView)
    }

    private fun setUpButton() {
        binding.btnSearchLocation.setOnClickListener {
            findNavController().navigate(R.id.nav_location_search_graf)
        }

        binding.btnSubmit.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.choosedLocation.collect {
                    if (it != null) {
                        viewModel.setPromiseLocation(it)
                    }
                }
            }
            findNavController().popBackStack()
        }
    }


    @SuppressLint("MissingPermission")
    private fun setCurrentLocation() {
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            if(viewModel.choosedLocation.value == null) {
                viewModel.chooseLocation(
                    Location(
                        GeoPoint(it.latitude, it.longitude),
                        CURRENT_LOCATION_TEXT
                    )
                )
            }
        }
    }

    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 15
        private const val CURRENT_LOCATION_TEXT = ""
    }
}