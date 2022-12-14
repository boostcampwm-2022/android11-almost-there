package com.woory.almostthere.presentation.ui.gaming

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeEventListener : SensorEventListener {

    private var shakeListener: OnShakeListener? = null
    private var shakeTimeStamp: Long = 0

    interface OnShakeListener {
        fun onShake()
    }

    fun setOnShakeListener(_shakeListener: OnShakeListener) {
        shakeListener = _shakeListener
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val gX = x / SensorManager.GRAVITY_EARTH
        val gY = y / SensorManager.GRAVITY_EARTH
        val gZ = z / SensorManager.GRAVITY_EARTH

        val gForce = sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            val now = System.currentTimeMillis()

            if (shakeTimeStamp + SHAKE_SLOP_TIME_MS > now) return

            shakeTimeStamp = now
            shakeListener?.onShake()
        }
    }

    companion object {
        private const val SHAKE_THRESHOLD_GRAVITY = 2.0F
        private const val SHAKE_SLOP_TIME_MS = 500
    }
}