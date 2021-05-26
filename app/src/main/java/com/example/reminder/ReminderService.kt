package com.example.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.AsyncTask.execute
import android.os.Build
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ReminderService : Service() {

    private val CHANNEL_ID = "channel_id_example_02"
    private val notificationId = 101
    private val url =
        "https://api.taapi.io/rsi?secret=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Im1hc2xhay53b2pjaWVjaEBnbWFpbC5jb20iLCJpYXQiOjE2MjE3OTU5MjMsImV4cCI6NzkyODk5NTkyM30.c0HQHOXrtlVO9uD6A0tCbNBK_yPS4lYyAOKFh-sA5g4&exchange=binance&symbol=CHZ/USDT&interval=1h&optInTimePeriod=7"

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        while (true)
        {
            val value = handleJson(HttpRequest().execute(url).get().toString())
            if (value >= 70)
                AsyncSendNotif().execute("Sprzedawaj")
            else if (value <= 30)
                AsyncSendNotif().execute("Kupuj")
            else
                Thread.sleep(60 * 1000)


        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel: NotificationChannel =
                NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(contentText : String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Wartość")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
    }
    private fun handleJson(jsonString :String) : Double {
        val jsonArray = JSONObject(jsonString)
        return jsonArray.getDouble("value")
    }

    inner class AsyncSendNotif : AsyncTask<String, String, Void>() {

        override fun doInBackground(vararg value: String): Void? {
            createNotificationChannel()
            sendNotification(value[0])
            return null
        }
    }
}