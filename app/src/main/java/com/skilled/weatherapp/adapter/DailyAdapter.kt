package com.skilled.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.skilled.weatherapp.databinding.ItemWeatherByDailyCardBinding
import com.skilled.weatherapp.models.Daily
import com.skilled.weatherapp.util.Constants
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter : RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    inner class DailyViewHolder(binding: ItemWeatherByDailyCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var dayName = binding.dayName
        var weatherIcon = binding.weatherIcon
        var humidity = binding.humidity
        var dayTemp = binding.dayTemp
        var nightTemp = binding.nightTemp
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyAdapter.DailyViewHolder {
        val binding =
            ItemWeatherByDailyCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyAdapter.DailyViewHolder, position: Int) {
        val dailyData = differ.currentList[position]
        holder.itemView.apply {
            val time = dailyData.dt.toLong()
            holder.dayName.text = SimpleDateFormat(
                "E",
                Locale.ENGLISH
            ).format(Date(time * 1000))
            Picasso.get()
                .load(Constants.IMAGE_PATH + dailyData.weather[0].icon + Constants.IMAGE_EXTENSION)
                .into(holder.weatherIcon)
            holder.humidity.text = dailyData.pop.toString()
            holder.dayTemp.text = dailyData.temp.day.toInt().toString() + "℃"
            holder.nightTemp.text = dailyData.temp.night.toInt().toString() + "℃"
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}