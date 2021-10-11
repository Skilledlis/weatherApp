package com.skilled.weatherapp.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skilled.weatherapp.models.OneCallResponse
import com.skilled.weatherapp.models.WeatherResult
import com.skilled.weatherapp.repository.Repository
import com.skilled.weatherapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class ViewModel(application: Application): AndroidViewModel(application) {

    private val weatherRepository: Repository = Repository()

    val weather: MutableLiveData<Resource<WeatherResult>> = MutableLiveData()
    val weatherOneCall: MutableLiveData<Resource<OneCallResponse>> = MutableLiveData()

    fun requestByCity(city: String) {
        GlobalScope.launch(Dispatchers.IO){
            weather.postValue(Resource.Loading())
            val response = weatherRepository.getWeatherByCityName(city)
            weather.postValue(handlerWeather(response))
        }
    }

    fun requestByCoordinates(lat: Double, lon: Double){
        GlobalScope.launch(Dispatchers.IO){
            weather.postValue(Resource.Loading())
            val response = weatherRepository.getWeatherByCoordinates(lat, lon)
            weather.postValue(handlerWeather(response))
        }
    }

    fun requestOneCall(lat: Double, lon: Double){
        GlobalScope.launch(Dispatchers.IO){
            weatherOneCall.postValue(Resource.Loading())
            val response = weatherRepository.getOneCall(lat, lon)
            weatherOneCall.postValue(handlerOneCall(response))
        }
    }

    private fun handlerWeather(response: Response<WeatherResult>): Resource<WeatherResult> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handlerOneCall(response: Response<OneCallResponse>): Resource<OneCallResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }



}