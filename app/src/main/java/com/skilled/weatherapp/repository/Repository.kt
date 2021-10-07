package com.skilled.weatherapp.repository

import com.skilled.weatherapp.api.RetrofitInstance

class Repository {
    suspend fun getWeatherByCityName(name: String) = RetrofitInstance.api.getWeatherByCityName(name = name)
    suspend fun getWeatherByCoordinates(lat: Double, lon:Double) = RetrofitInstance.api.getWeatherByCoordinates(lat = lat, lon = lon)
    suspend fun getOneCall(lat: Double, lon:Double) = RetrofitInstance.api.getOneCall(lat = lat, lon = lon)
}