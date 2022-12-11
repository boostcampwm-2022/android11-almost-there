package com.woory.presentation.ui.gaming

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.woory.presentation.R
import com.woory.presentation.databinding.DialogFragmentShakeDeviceBinding
import com.woory.presentation.ui.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class ShakeDeviceFragment :
    BaseDialogFragment<DialogFragmentShakeDeviceBinding>(R.layout.dialog_fragment_shake_device) {

    private val viewModel: GamingViewModel by activityViewModels()

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var shakeEventListener: ShakeEventListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        showSetArriveButton()
        setShakeEventListener()
    }

    override fun onResume() {
        super.onResume()
        accelerometer.also { accelerometer ->
            sensorManager.registerListener(
                shakeEventListener,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeEventListener)
    }

    private fun setShakeEventListener() {
        shakeEventListener = ShakeEventListener().apply {
            setOnShakeListener(object : ShakeEventListener.OnShakeListener {
                override fun onShake() {
                    setUserArrived()
                }
            })
        }
    }

    private fun showSetArriveButton() {
        lifecycleScope.launch {
            delay(SHAKE_WAIT_TIME)
            binding.btnArrive.visibility = View.VISIBLE
            binding.btnArrive.setOnClickListener {
                setUserArrived()
            }
        }
    }

    private fun setUserArrived() {
        lifecycleScope.launch {
            val gameCode = runBlocking { viewModel.gameCode.first() }
            val uid = runBlocking { viewModel.myUserInfo.userID }
            viewModel.setUserArrived(gameCode, uid)
        }
        this.dismiss()
    }

    companion object {
        const val SHAKE_WAIT_TIME: Long = 5000
    }
}