package dev.bananaumai.practices.service.started

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    val tag = this.javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Intent(this, SimpleIntentService::class.java)
            .apply { putExtra(Intent.EXTRA_TEXT, "ONE") }
            .also { startService(it) }

        Intent(this, SimpleIntentService::class.java)
            .apply { putExtra(Intent.EXTRA_TEXT, "TWO")}
            .also { startService(it) }

        Intent(this, SimpleService::class.java)
            .apply { putExtra(Intent.EXTRA_TEXT, "THREE")}
            .also { startService(it) }
            .also { Log.d(tag, "started SimpleService") }


        Intent(this, SimpleService::class.java)
            .apply { putExtra(Intent.EXTRA_TEXT, "FOUR")}
            .also { startService(it) }
            .also { Log.d(tag, "started SimpleService") }

        Log.d(tag, "End of onCreate()")
    }
}
