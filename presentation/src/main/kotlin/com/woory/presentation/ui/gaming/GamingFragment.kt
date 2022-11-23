package com.woory.presentation.ui.gaming

import android.graphics.PointF
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView.OnClickListenerCallback
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.MAP_API_KEY
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentGamingBinding

class GamingFragment : BaseFragment<FragmentGamingBinding>(R.layout.fragment_gaming) {

    private var marker: TMapMarkerItem? = null

    private val behavior by lazy {
        BottomSheetBehavior.from(binding.layoutBottomSheet.layoutBottomSheet)
    }

    private val bitmap by lazy {
        ContextCompat.getDrawable(requireActivity(), R.drawable.bg_speech_bubble)?.toBitmap()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Todo :: 이하 코드 리펙토링 필요
        binding.mapGaming.apply {
            setSKTMapApiKey(MAP_API_KEY)
            setOnMapReadyListener {
                setMarker()
            }

            setOnClickListenerCallback(object : OnClickListenerCallback {
                override fun onPressDown(
                    p0: ArrayList<TMapMarkerItem>?,
                    p1: ArrayList<TMapPOIItem>?,
                    p2: TMapPoint?,
                    p3: PointF?
                ) {
                    if (p0?.isNotEmpty() == true) {
                        showBottomSheet()
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
    }

    // Todo :: 테스트용 마커 생성 함수
    private fun setMarker() {
        val lat = 37.468954
        val lon = 126.4544153

        if (marker == null) {
            marker = TMapMarkerItem().apply {
                id = "My Location"
                icon = bitmap
            }
        }

        val curPoint = TMapPoint(lat, lon)
        requireNotNull(marker).tMapPoint = curPoint

        binding.mapGaming.apply {
            setCenterPoint(lat, lon)
            zoomLevel = 13
            removeTMapMarkerItem("My Location")
            addTMapMarkerItem(marker)
        }
    }

    private fun showBottomSheet() {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun dismissBottomSheet() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
}