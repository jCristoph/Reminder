package com.example.reminder.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reminder.R
import com.example.reminder.api.Repository
import com.example.reminder.const.Constants
import com.example.reminder.helper.getNumberDescription
import com.example.reminder.model.Response
import retrofit2.Call
import retrofit2.Callback


class ReminderService : Service() {
    private var repository: Repository = Repository()
    private var lastShownNotificationId = 0

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundServiceWhenSdkAbove26()
        getData()
        return START_STICKY
    }

    private fun scheduleNext() {
        Handler(Looper.getMainLooper()).postDelayed({
            getData()
        }, Constants.INTERVAL * 1000L)
    }

    private fun sendNotification(message: String?) {
        val builder = getNotificationBuilder(
            this,
            Constants.CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )

        val notification = builder
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message).build()

        val nm = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(Constants.NOTIFICATION_ID, notification)
        if (Constants.NOTIFICATION_ID != lastShownNotificationId) {
            // Cancel previous notification
            nm.cancel(lastShownNotificationId)
        }
        lastShownNotificationId = Constants.NOTIFICATION_ID
    }

    private fun getNotificationBuilder(
        context: Context?,
        channelId: String?,
        importance: Int
    ): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prepareChannel(context!!, channelId!!, importance)
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareChannel(context: Context, id: String, importance: Int) {
        val appName: String = context.getString(R.string.app_name)
        val description: String =
            context.resources.getString(R.string.notifications_channel_description)
        val nm = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var channel = nm.getNotificationChannel(id)
        if (channel == null) {
            channel = NotificationChannel(id, appName, importance)
            channel.description = description
            nm.createNotificationChannel(channel)
        }
    }


    private fun getData() {
        repository.getNumber().enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val number = response.body()?.value
                    if (number != null) {
                        getNumberDescription(number)?.let {
                            sendNotification(it)
                        }
                    }
                    scheduleNext()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                sendNotification(getString(R.string.error))
                scheduleNext()
            }
        })
    }

    private fun startForegroundServiceWhenSdkAbove26() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_ID,
                getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
            val notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build()
            startForeground(1, notification)
        }
    }
}