package com.skilled.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.skilled.weatherapp.R
import com.skilled.weatherapp.databinding.ItemWeatherCardBinding
import com.skilled.weatherapp.models.Hourly
import com.skilled.weatherapp.util.Constants
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    inner class HourlyViewHolder(binding: ItemWeatherCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var time = binding.time
        var weatherIcon = binding.weatherIcon
        var humidity = binding.humidity
        var temp = binding.temp
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Hourly>() {
        override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyAdapter.HourlyViewHolder {
        val binding =
            ItemWeatherCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyAdapter.HourlyViewHolder, position: Int) {
        val hourlyData = differ.currentList[position]
        holder.itemView.apply {
            val time = hourlyData.dt.toLong()
            holder.time.text = SimpleDateFormat(
                "HH:mm",
                Locale.ENGLISH
            ).format(Date(time * 1000))
            Picasso.get()
                .load(Constants.IMAGE_PATH + hourlyData.weather[0].icon + Constants.IMAGE_EXTENSION)
                .into(holder.weatherIcon)
            holder.humidity.text = hourlyData.pop.toInt().toString()
            holder.temp.text = hourlyData.temp.toInt().toString() + "℃"
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}