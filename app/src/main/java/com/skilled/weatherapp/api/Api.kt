package com.skilled.weatherapp.api

import com.skilled.weatherapp.models.OneCallResponse
import com.skilled.weatherapp.models.WeatherResult
import com.skilled.weatherapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("appid")
        appId: String = API_KEY,
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("units")
        units: String = "metric"
    ):Response<WeatherResult>

    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query("appid")
        appId: String = API_KEY,
        @Query("q")
        name: String,
        @Query("units")
        units: String = "metric"
    ):Response<WeatherResult>

    @GET("onecall")
    suspend fun getOneCall(
        @Query("appid")
        appID: String = API_KEY,
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("units")
        units: String = "metric",
        @Query("exclude")
        exclude: String = "alerts,minutely,current"
    ):Response<OneCallResponse>
}