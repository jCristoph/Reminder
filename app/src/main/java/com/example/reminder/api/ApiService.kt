package com.example.reminder.api

import com.example.reminder.model.Response
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // TODO: it shouldn't be like that. All secrets need to be hidden - look below
    // https://stackoverflow.com/questions/14570989/best-practice-for-storing-and-protecting-private-api-keys-in-applications
    @GET("rsi?secret=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Im1hc2xhay53b2pjaWVjaEBnbWFpbC5jb20iLCJpYXQiOjE2MjE3OTU5MjMsImV4cCI6NzkyODk5NTkyM30.c0HQHOXrtlVO9uD6A0tCbNBK_yPS4lYyAOKFh-sA5g4&exchange=binance&symbol=CHZ/USDT&interval=1h&optInTimePeriod=7")
    fun getNumber(): Call<Response>
}