package com.skilled.weatherapp.repository

import com.skilled.weatherapp.api.RetrofitInstance

class Repository {
    suspend fun getWeatherByCityName(name: String) = RetrofitInstance.api.getWeatherByCityName(name = name)
    suspend fun getWeatherByCoordinates(lat: Long, lon:Long) = RetrofitInstance.api.getWeatherByCoordinates(lat = lat, lon = lon)
}