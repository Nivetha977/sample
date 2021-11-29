package com.example.sample_app.ui.weather.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sample_app.databinding.FragmentFiveDaysForecastBinding
import com.example.sample_app.ui.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FiveDaysForecastFragment(var lat: String?, var lng: String?) : Fragment() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var binding: FragmentFiveDaysForecastBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFiveDaysForecastBinding.inflate(layoutInflater)
        weatherViewModel.getFiveDaysForecast(lat, lng)

        return binding.root
    }


}