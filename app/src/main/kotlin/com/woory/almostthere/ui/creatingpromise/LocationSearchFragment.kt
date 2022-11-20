package com.woory.almostthere.ui.creatingpromise

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.skt.tmap.TMapData
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentLocationSearchBinding
import com.woory.almostthere.model.LocationModel
import com.woory.almostthere.ui.BaseFragment
import com.woory.almostthere.util.MAP_API_KEY
import com.woory.almostthere.util.REQUIRE_PERMISSION_TEXT
import kotlinx.coroutines.launch

class LocationSearchFragment :
    BaseFragment<FragmentLocationSearchBinding>(R.layout.fragment_location_search),
    SearchView.OnQueryTextListener {

    private val activityViewModel: CreatingPromiseViewModel by lazy {
        ViewModelProvider(requireActivity())[(CreatingPromiseViewModel::class.java)]
    }

    private val fragmentViewModel: LocationSearchViewModel by lazy {
        ViewModelProvider(this)[LocationSearchViewModel::class.java]
    }

    private val locationManager by lazy {
        requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private val tMapData = TMapData()

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
        binding.mapPromiseLocationPick.apply {
            setSKTMapApiKey(MAP_API_KEY)
            setOnMapReadyListener {
                fragmentViewModel.setMapReady(true)
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
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                fragmentViewModel.location.collect {
                    if (it != null) {
                        binding.mapPromiseLocationPick.setCenterPoint(it.latitude, it.longitude)
                    }
                }
            }
        }

        binding.btnSearchLocation.apply {
            setOnQueryTextListener(this@LocationSearchFragment)
        }

        binding.btnSubmit.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                fragmentViewModel.location.collect {
                    if (it != null && it.location != "") {
                        activityViewModel.setPromiseLocation(it)
                    }
                }
            }
            findNavController().popBackStack()
        }
    }

    @SuppressLint("MissingPermission")
    private fun setCurrentLocation() {
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            fragmentViewModel.setPromiseLocation(
                LocationModel(
                    it.latitude,
                    it.longitude,
                    CURRENT_LOCATION_TEXT
                )
            )
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val queryString = if (query.isNullOrEmpty()) "" else query.toString()
        tMapData.findAllPOI(queryString) { queryResult ->
            lifecycleScope.launch {
                fragmentViewModel.isMapReady.collect {
                    if (queryResult.isNullOrEmpty().not() && it) {
                        val res = queryResult[0]
                        fragmentViewModel.setPromiseLocation(
                            LocationModel(
                                res.noorLat.toDouble(),
                                res.noorLon.toDouble(),
                                queryString
                            )
                        )
                    }
                }
            }
        }
        binding.btnSearchLocation.onActionViewCollapsed()
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean = false

    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 15
        private const val CURRENT_LOCATION_TEXT = ""
    }
}