package com.example.sample_app.ui.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sample_app.network.Resource
import com.example.sample_app.ui.weather.model.CurrentWeatherResponse
import com.example.sample_app.ui.weather.model.FiveDaysForecastResponse
import com.example.sample_app.ui.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Created by Nivetha S on 25-11-2021.
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val _weatherResponse = MutableLiveData<Resource<CurrentWeatherResponse>>()
    val weatherResponse: LiveData<Resource<CurrentWeatherResponse>>
        get() = _weatherResponse

    private val _fiveDaysForecastResponse = MutableLiveData<Resource<FiveDaysForecastResponse>>()
    val fiveDaysForecastResponse: LiveData<Resource<FiveDaysForecastResponse>>
        get() = _fiveDaysForecastResponse

    fun getCurrentWeather(lat: String?, lng: String?) {

        CoroutineScope(Dispatchers.IO).launch {
            weatherRepository.getCurrentWeather(lat, lng).let {
                if (it.isSuccessful) {
                    _weatherResponse.postValue(Resource.success(it.body()))
                } else {
                    _weatherResponse.postValue(Resource.error(it.errorBody().toString(), null))
                }
            }

        }

    }

    fun getFiveDaysForecast(lat: String?, lng: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepository.getFiveDaysForecast(lat, lng).let {
                if (it.isSuccessful) {
                    _fiveDaysForecastResponse.postValue(Resource.success(it.body()))
                } else {
                    _fiveDaysForecastResponse.postValue(
                        Resource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }

        }
    }

}