package dev.bananaumai.practices.service.started

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import kotlin.random.Random

class SimpleIntentService : IntentService("SimpleIntentService") {
    private val tag: String = this.javaClass.name
    private val rand: Random = Random.Default

    override fun onCreate() {
        Log.d(tag, "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand - intent : $intent (${intent?.extras?.get(Intent.EXTRA_TEXT)})")
        Log.d(tag, "onStartCommand - flags : $flags")
        Log.d(tag, "onStartCommand - startId : $startId")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        try {
            Thread.sleep(rand.nextLong(until = 2000))
            Log.d(tag, "${intent?.extras?.get(Intent.EXTRA_TEXT)} Working in ${Thread.currentThread().name}(${Thread.currentThread().id})")
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy")
        super.onDestroy()
    }
}

class SimpleService : Service() {
    private val tag: String = this.javaClass.name
    private val rand: Random = Random.Default
    private lateinit var serviceHandler: ServiceHandler

    private inner class ServiceHandler(looper: Looper): Handler(looper) {
        override fun handleMessage(msg: Message?) {
            try {
                val str = msg?.obj as String? ?: "Unknown"

                Thread.sleep(rand.nextLong(until = 2000))

                Log.d(tag, "$str Working in ${Thread.currentThread().name}(${Thread.currentThread().id})")
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }

            stopSelf(msg!!.arg1)
        }
    }

    override fun onCreate() {
        Log.d(tag, "onCreate")
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceHandler = ServiceHandler(looper)
        }
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "SimpleService starting", Toast.LENGTH_SHORT).show()
        Log.d(tag, "onStartCommand - intent : $intent (${intent?.extras?.get(Intent.EXTRA_TEXT)})")
        Log.d(tag, "onStartCommand - flags : $flags")
        Log.d(tag, "onStartCommand - startId : $startId")

        serviceHandler.obtainMessage().also { msg ->
            msg.arg1 = startId
            msg.obj = intent?.extras?.get(Intent.EXTRA_TEXT)

            serviceHandler.sendMessage(msg)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // not allowed to bind
        return null
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy")
        super.onDestroy()
    }
}