package com.example.sample_app.ui.weather.model

import com.google.gson.annotations.SerializedName

class Main {

    @SerializedName("temp")
     val temp = 0.0

    @SerializedName("temp_min")
    private val tempMin = 0.0

    @SerializedName("grnd_level")
    private val grndLevel = 0.0

    @SerializedName("humidity")
     val humidity = 0

    @SerializedName("pressure")
    private val pressure = 0.0

    @SerializedName("sea_level")
    private val seaLevel = 0.0

    @SerializedName("temp_max")
    private val tempMax = 0.0

}
