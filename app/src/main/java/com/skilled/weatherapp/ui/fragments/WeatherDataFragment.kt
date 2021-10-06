package com.skilled.weatherapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.appbar.AppBarLayout
import com.skilled.weatherapp.databinding.WeatherDataFragmentBinding
import com.skilled.weatherapp.ui.viewmodel.ViewModel
import com.skilled.weatherapp.util.Resource
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class WeatherDataFragment : Fragment() {

    private var _binding: WeatherDataFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var viewModel: ViewModel

    private lateinit var locationName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherDataFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel.requestCity()
        collapseTitleBar()

        viewModel.weather.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        Picasso.get()
                            .load("https://openweathermap.org/img/wn/" + it.weather[0].icon + "@2x.png")
                            .into(binding.imageView3)
                        binding.location.text = it.name
                        locationName = it.name
                        val updateAt: Long = it.dt.toLong()
                        binding.updateAt.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(updateAt*1000))
                        binding.temp.text = it.main.temp.toInt().toString() + "℃"
                        binding.maxTemp.text = it.main.temp_max.toInt().toString() + "℃"
                        binding.minTemp.text = it.main.temp_min.toInt().toString() + "℃"
                        binding.feelsLikeTemp.text = it.main.feels_like.toInt().toString() + "℃"
                        binding.description.text = it.weather[0].description.capitalize()
                    }
                }
            }

        })
    }

    private fun collapseTitleBar() {
        var isShow = true
        var scrollRange = -1
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.collapsingToolbar.title = locationName
                isShow = true
                binding.collapseToolBarConstLayot.visibility = View.INVISIBLE
            } else if (isShow) {
                binding.collapsingToolbar.title =
                    " "//careful there should a space between double quote otherwise it wont work
                binding.collapseToolBarConstLayot.visibility = View.VISIBLE
                isShow = false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}