package com.skilled.weatherapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skilled.weatherapp.models.WeatherResult
import com.skilled.weatherapp.repository.Repository
import com.skilled.weatherapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class ViewModel: ViewModel() {


    private val weatherRepository: Repository = Repository()

    val weather: MutableLiveData<Resource<WeatherResult>> = MutableLiveData()


    fun requestCity() {
        GlobalScope.launch(Dispatchers.IO){
            weather.postValue(Resource.Loading())
            val response = weatherRepository.getWeatherByCityName("London")
            weather.postValue(handlerMovie(response))
        }
    }

    private fun handlerMovie(response: Response<WeatherResult>): Resource<WeatherResult> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}