package com.example.sample_app.ui.weather.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.sample_app.databinding.FragmentCurrentWeatherBinding
import com.example.sample_app.network.Status
import com.example.sample_app.ui.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CurrentWeatherFragment(var lat: String?, var lng: String?) : Fragment() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var binding: FragmentCurrentWeatherBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCurrentWeatherBinding.inflate(layoutInflater)

        weatherViewModel.getCurrentWeather(lat, lng)
        weatherViewModel.weatherResponse.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.e("Nive ", "onCreate:sucess response ${it.data?.main?.temp}")
                    binding.textViewTemp.text = String.format(
                        Locale.getDefault(), "%.0f°", it.data?.main?.temp
                    )

                    binding.textViewHumanity.text =
                        "Humanity: ".plus(" ").plus(it.data?.main?.humidity).plus("%")
                    binding.textViewWind.text =
                        "Wind".plus(" ").plus(it.data?.wind?.speed).plus("km/hr")

                }
            }
        })
        return binding.root
    }

}