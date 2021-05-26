package com.example.reminder

import android.os.AsyncTask
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class HttpRequest : AsyncTask<String, String, String>() {
    override fun doInBackground(vararg url: String?): String {
        var text :String
        val connection = URL(url[0]).openConnection() as HttpURLConnection
        try{
            connection.connect()
            text = connection.inputStream.use { it.reader().use{reader -> reader.readText()}}
        } finally {
            connection.disconnect()
        }
        return text
    }

}

