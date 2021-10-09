package com.skilled.weatherapp.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.appbar.AppBarLayout
import com.skilled.weatherapp.R
import com.skilled.weatherapp.adapter.DailyAdapter
import com.skilled.weatherapp.adapter.HourlyAdapter
import com.skilled.weatherapp.databinding.WeatherDataFragmentBinding
import com.skilled.weatherapp.models.Daily
import com.skilled.weatherapp.ui.viewmodel.ViewModel
import com.skilled.weatherapp.util.Constants
import com.skilled.weatherapp.util.Constants.Companion.IMAGE_EXTENSION
import com.skilled.weatherapp.util.Constants.Companion.IMAGE_PATH
import com.skilled.weatherapp.util.LocationPermission.hasLocationPermission
import com.skilled.weatherapp.util.Resource
import com.squareup.picasso.Picasso
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.text.SimpleDateFormat
import java.util.*

class WeatherDataFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private var _binding: WeatherDataFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var viewModel: ViewModel
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //    private lateinit var locationManager: LocationManager
    var gpsStatus = false

    private lateinit var locationName: String
    var lat: Double = 1212.1
    var lon: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherDataFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        setupRecyclerView()
        checkGpsStatus()
        loadData()
        updateData()

        binding.progressBar.setOnClickListener{
            checkGpsStatus()
            loadData()
            updateData()
        }


        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        viewModel.requestCity()

        collapseTitleBar()


        viewModel.weather.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        Picasso.get()
                            .load(IMAGE_PATH + it.weather[0].icon + IMAGE_EXTENSION)
                            .into(binding.weatherIcon)
                        binding.location.text = it.name
                        locationName = it.name
                        val updateAt: Long = it.dt.toLong()
                        binding.updateAt.text = SimpleDateFormat(
                            "dd/MM HH:mm",
                            Locale.ENGLISH
                        ).format(Date(updateAt * 1000))
                        binding.temp.text = it.main.temp.toInt().toString() + "℃"
                        binding.maxTemp.text = it.main.temp_max.toInt().toString() + "℃"
                        binding.minTemp.text = it.main.temp_min.toInt().toString() + "℃"
                        binding.feelsLikeTemp.text = it.main.feels_like.toInt().toString() + "℃"
                        binding.description.text = it.weather[0].description.capitalize()
                    }
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        viewModel.weatherOneCall.observe(viewLifecycleOwner, {responce ->
            when(responce){
                is Resource.Success ->{
                    responce.data?.let {
                        hourlyAdapter.differ.submitList(it.hourly)
                        dailyAdapter.differ.submitList(it.daily)
                    }
                }
            }
        })
        loadData()
    }

    @SuppressLint("MissingPermission")
    private fun updateData() {
        if (hasLocationPermission(requireContext()) && gpsStatus && isOnOnline()) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                lat = location.latitude
                lon = location.longitude
                viewModel.requestByCoordinates(lat, lon)
                viewModel.requestOneCall(lat,lon)
                saveData()
            }
            Log.d("WeatherDataFragment", "onCreate11111:  $lat")

        } else {
            EasyPermissions.requestPermissions(
                this,
                "This application cannot work without Location Permission.",
                Constants.PERMISSION_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    private fun saveData() {
        val sharedPreferences = requireActivity().applicationContext.getSharedPreferences(
            "Shared Preference",
            Context.MODE_PRIVATE
        )
        val edit = sharedPreferences.edit()
        edit.apply {
            putString("LAT", lat.toString())
            putString("LON", lon.toString())
        }.apply()

        Log.d("WeatherDataFragment", "saveDate:  $lat")
    }

    private fun loadData() {
        val sharedPreferences = requireActivity().applicationContext.getSharedPreferences(
            "Shared Preference",
            Context.MODE_PRIVATE
        )
        val savedLat = sharedPreferences.getString("LAT", "1.1")
        val savedLon = sharedPreferences.getString("LON", "1.1")

        lat = java.lang.Double.parseDouble(savedLat)
        lon = java.lang.Double.parseDouble(savedLon)

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
                binding.collapsingToolbar.title = " "
                binding.collapseToolBarConstLayot.visibility = View.VISIBLE
                isShow = false
            }
        })
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireContext()).build().show();
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }

    private fun checkGpsStatus() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gpsStatus) {
            return
        } else {

            AlertDialog.Builder(requireContext()).setTitle(R.string.Please_check_your_GPS)
                .setMessage(R.string.this_application_cannot_work_without_location_go_to_gps_settings)
                .setPositiveButton(
                    R.string.yes,
                    { _: DialogInterface, _: Int -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
                .setNegativeButton(
                    R.string.no,
                    { _: DialogInterface, _: Int -> activity?.finish() })
                .show()
        }
    }

    private fun isOnOnline(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return if (capabilities != null) {
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.Please_check_your_network_connection)
                .setMessage(R.string.this_application_cannot_work_without_network_go_to_gps_settings)
                .setPositiveButton(
                    R.string.yes,
                    { _: DialogInterface, _: Int -> startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) })
                .setNegativeButton(
                    R.string.no, { _: DialogInterface, _: Int -> activity?.finish() })
                .show()
            return false
        }
    }

    private fun setupRecyclerView() {
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        binding.hourlyRv.apply {
            adapter = hourlyAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.dailyRv.apply {
            adapter = dailyAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}