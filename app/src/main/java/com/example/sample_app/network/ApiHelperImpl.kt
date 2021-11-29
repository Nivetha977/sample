package com.example.sample_app.network

import com.example.sample_app.R
import com.example.sample_app.app.App
import com.example.sample_app.ui.weather.model.CurrentWeatherResponse
import com.example.sample_app.ui.weather.model.FiveDaysForecastResponse
import retrofit2.Response
import javax.inject.Inject

/**
 *Created by Nivetha S on 19-11-2021.
 */
class ApiHelperImpl @Inject constructor(private val apiService: ApiService) {

    suspend fun getCurrentWeatherData(
        lat: String?, lng: String?
    ): Response<CurrentWeatherResponse> = apiService.getCurrentWeather(
        lat, lng, "a872c8e5e4ef39a803433f74d5f73192"
    )

    suspend fun getFiveDaysForecast(
        lat: String?, lng: String?
    ): Response<FiveDaysForecastResponse> = apiService.getFiveDaysForecast(
        lat, lng, "a872c8e5e4ef39a803433f74d5f73192"
    )

}