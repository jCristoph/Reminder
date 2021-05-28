package com.example.reminder.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reminder.api.Repository
import com.example.reminder.model.Response
import retrofit2.Call
import retrofit2.Callback

class MainViewModel: ViewModel(){
    // TODO: repository  and ViewModel should be injected as a parameter with DI (see Koin, Dagger2, Hilt)
    private var repository: Repository? = null

    private var _data = MutableLiveData(0.0)
    var data = _data

    init {
        repository = Repository()
    }

    fun getData () {
        repository?.getNumber()?.enqueue(object: Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful){
                    val number = response.body()?.value
                    _data.postValue(number)
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                _data.postValue(0.0)
            }
        })
    }
}