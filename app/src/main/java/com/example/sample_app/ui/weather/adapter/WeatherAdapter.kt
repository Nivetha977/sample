package com.example.sample_app.ui.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sample_app.databinding.ItemAddressBinding
import com.example.sample_app.databinding.ItemWeatherBinding
import com.example.sample_app.ui.history.adapter.HistoryAdapter

/**
 *Created by Nivetha S on 26-11-2021.
 */
class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapter.ViewHolder {
        val binding =
            ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherAdapter.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 4
    }

    class ViewHolder(var binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}