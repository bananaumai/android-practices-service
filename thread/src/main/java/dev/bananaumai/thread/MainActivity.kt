package dev.bananaumai.thread

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    val tag = this.javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread(Runnable {
            while(!Thread.currentThread().isInterrupted) {
                Log.d("$tag - inside thread", "Here it is! ${Thread.currentThread().name}(${Thread.currentThread().id})")
                Thread.sleep(5000)
            }
            Log.d("$tag - inside thread", "Interrupted!")
        }).start()
    }
}
