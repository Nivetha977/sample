package com.example.sample_app.ui.weather.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sample_app.ui.weather.fragment.CurrentWeatherFragment
import com.example.sample_app.ui.weather.fragment.FiveDaysForecastFragment

/**
 *Created by Nivetha S on 29-11-2021.
 */
class TabLayoutAdapter(
    manager: FragmentManager,
    context: Context,
    var totalTabs: Int,
    val lat: String?,
    val lng: String?
) :
    FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return CurrentWeatherFragment(lat,lng)
            }
            1 -> {
                return FiveDaysForecastFragment(lat,lng)
            }

            else -> return CurrentWeatherFragment(lat, lng)
        }

        return CurrentWeatherFragment(lat, lng)
    }

}