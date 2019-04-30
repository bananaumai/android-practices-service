package dev.bananaumai.bound

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.util.Log

class DataEmitter : Service() {
    private val tag = this.javaClass.name

    private val binder: LocalBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService() = this@DataEmitter
    }

    interface EventListener {
        fun listen()
    }

    inner class Accelerometer(private var manager: SensorManager? = null) : EventListener {
        init {
            if (manager == null) {
                manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            }
        }

        private inner class Listener : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.getType() != Sensor.TYPE_ACCELEROMETER) {
                    return
                }

                Log.d(tag, "${event.timestamp / 1000000L}: ${event.values.joinToString(", ")}")
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            }
        }

        override fun listen() {
            val sensor = manager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            manager!!.registerListener(Listener(), sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onCreate() {
        Log.d(tag, "onCreate")

        super.onCreate()

        val accelerometer = Accelerometer()
        accelerometer.listen()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(tag, "onBind")
        return binder
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy")

        super.onDestroy()
    }
}