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

    override fun onCreate() {
        Log.d(tag, "onCreate")

        super.onCreate()

        val logger = { data: Any ->
            Log.d(tag, data.toString())
            Unit
        }

        val accelerometer = Accelerometer(getSystemService(Context.SENSOR_SERVICE) as SensorManager)
        accelerometer.listen(logger)
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

interface EventListener {
    fun listen(callback: (Any) -> Unit)
}

class Accelerometer(private val manager: SensorManager) : EventListener {
    inner class AccelerometerEvent(private val event: SensorEvent) {
        override fun toString() =
            "${this.javaClass.name} { timestamp: ${event.timestamp}, values: ${event.values.joinToString(",", "[", "]")} }"
    }

    override fun listen(callback: (Any) -> Unit) {
        val sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.getType() != Sensor.TYPE_ACCELEROMETER) {
                    return
                }

                callback(AccelerometerEvent(event))
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            }
        }
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
    }
}