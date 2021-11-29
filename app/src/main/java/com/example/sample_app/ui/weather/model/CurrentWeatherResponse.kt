package com.example.sample_app.ui.weather.model

import com.google.gson.annotations.SerializedName

/**
 *Created by Nivetha S on 25-11-2021.
 */
class CurrentWeatherResponse {

    @SerializedName("dt")
    private var dt = 0

    @SerializedName("weather")
     var weather: List<WeatherItem?>? = null

    @SerializedName("name")
    private var name: String? = null

    @SerializedName("cod")
    private var cod = 0

    @SerializedName("main")
     var main: Main? = null

    @SerializedName("id")
    private var id = 0

    @SerializedName("base")
    private var base: String? = null

    @SerializedName("wind")
     var wind: Wind? = null

}