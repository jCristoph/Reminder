package com.example.reminder

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var text : TextView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startCoroutine()

    }
    private fun startCoroutine() = runBlocking {
        delay(1000L)// this: CoroutineScope
        launch { // launch a new coroutine and continue
            startService(Intent(this@MainActivity, ReminderService::class.java))
        }
    }
}