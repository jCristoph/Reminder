package com.example.reminder.api

import com.example.reminder.model.Response
import retrofit2.Call

class Repository {
    fun getNumber(): Call<Response> {
        val request = ServiceBuilder.buildService(ApiService::class.java)
        return request.getNumber()
    }
}