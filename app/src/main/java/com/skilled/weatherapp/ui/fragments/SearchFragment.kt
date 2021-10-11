package com.skilled.weatherapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skilled.weatherapp.databinding.FragmentSeachBinding
import com.skilled.weatherapp.ui.viewmodel.ViewModel
import com.skilled.weatherapp.util.Constants
import com.skilled.weatherapp.util.Resource
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : Fragment() {

    private var _binding: FragmentSeachBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: ViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeachBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        var dt: Long = 0

        viewModel.weather.observe(viewLifecycleOwner, { responce ->
            when (responce) {
                is Resource.Success -> {
                    responce.data?.let {
                        binding.cityName.text = it.name
                        binding.description.text = it.weather[0].description
                        binding.temp.text = it.main.temp.toInt().toString() + "℃"
                        binding.tempMax.text = it.main.temp_max.toInt().toString() + "℃"
                        binding.tempMin.text = it.main.temp_min.toInt().toString() + "℃"
                        Picasso.get()
                            .load(Constants.IMAGE_PATH + it.weather[0].icon + Constants.IMAGE_EXTENSION)
                            .into(binding.weatherIcon)
                        binding.to.text = "to"


                        binding.shareButton.visibility = View.VISIBLE
                        dt = it.dt.toLong()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "City ${responce.message?.lowercase()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.requestByCity(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.shareButton.setOnClickListener {
            activity?.let {
                viewModel.weather.value?.let {

                }
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    val dtText = SimpleDateFormat(
                        "dd/MM HH:mm",
                        Locale.ENGLISH
                    ).format(Date(dt * 1000))
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "${binding.cityName.text} \n" +
                                "${binding.temp.text} \n" +
                                "${binding.description.text} \n" +
                                dtText
                    )
                    putExtra(Intent.EXTRA_SUBJECT, "Weather")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)

                it.startActivity(shareIntent)
            }

        }


        return binding.root
    }
}