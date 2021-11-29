package com.example.sample_app.ui.history.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sample_app.databinding.ItemAddressBinding
import com.example.sample_app.db.Address
import com.example.sample_app.ui.history.view.HistoryFragment
import com.example.sample_app.ui.weather.view.WeatherActivity

/**
 *Created by Nivetha S on 24-11-2021.
 */
class HistoryAdapter(
    private val fragmentActivity: HistoryFragment,
    private val addressList: List<Address>
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val binding =
            ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {

        with(holder.binding) {

            textViewAddress.text = HtmlCompat.fromHtml(
                "<font color='blue'> <u>Address: </u> </font>" + " " + addressList[position].address,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            textViewCoOrdinates.text = HtmlCompat.fromHtml(
                "<font color='blue'> <u>Co-ordinates: </u> </font>" + " " + addressList[position].lat + "," + addressList[position].lng,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            cardView.setOnClickListener {
                val myIntent = Intent(
                    fragmentActivity.activity,
                    WeatherActivity::class.java
                )
                myIntent.putExtra("lat", addressList[position].lat.toString())
                myIntent.putExtra("lng", addressList[position].lng.toString())
                fragmentActivity.startActivity(myIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    class ViewHolder(var binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}