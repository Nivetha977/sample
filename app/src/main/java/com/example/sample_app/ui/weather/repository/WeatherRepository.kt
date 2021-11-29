package com.example.sample_app.ui.weather.repository

import com.example.sample_app.network.ApiHelperImpl
import javax.inject.Inject

/**
 *Created by Nivetha S on 24-11-2021.
 */
class WeatherRepository @Inject constructor(var apiHelperImpl: ApiHelperImpl) {

    suspend fun getCurrentWeather(lat: String?,lng:String?) = apiHelperImpl.getCurrentWeatherData(lat,lng)

    suspend fun getFiveDaysForecast(lat: String?,lng:String?) = apiHelperImpl.getFiveDaysForecast(lat,lng)


}