package com.example.sample_app.ui.weather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.sample_app.databinding.ActivityWeatherBinding
import com.example.sample_app.ui.weather.adapter.TabLayoutAdapter
import com.example.sample_app.ui.weather.viewmodel.WeatherViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private var lng: String? = null
    private var lat: String? = null

    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Current Weather"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("5 days Forecast"))
        if (intent != null) {
            lat = intent.getStringExtra("lat")
            lng = intent.getStringExtra("lng")
        }

        val adapter = TabLayoutAdapter(supportFragmentManager, this, binding.tabLayout.tabCount,lat,lng)
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }
}