package com.example.sample_app.network

import com.example.sample_app.ui.weather.model.CurrentWeatherResponse
import com.example.sample_app.ui.weather.model.FiveDaysForecastResponse
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *Created by Nivetha S on 19-11-2021.
 */
interface ApiService {


    @GET(ApiNames.GET.WEATHER)
    suspend fun getCurrentWeather(
        @Query("lat") lat: String?,
        @Query("lon") lng: String?,
        @Query("appid") appId: String?
    ): Response<CurrentWeatherResponse>

    @GET(ApiNames.GET.FORECAST)
    suspend fun getFiveDaysForecast(
        @Query("lat") lat: String?,
        @Query("lon") lng: String?,
        @Query("appid") appId: String?
    ): Response<FiveDaysForecastResponse>

}