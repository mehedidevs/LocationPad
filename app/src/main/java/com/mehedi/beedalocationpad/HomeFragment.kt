package com.mehedi.beedalocationpad

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mehedi.beedalocationpad.base.BaseFragment
import com.mehedi.beedalocationpad.databinding.FragmentHomeBinding
import com.mehedi.beedalocationpad.utils.PermissionUtils
import java.io.IOException
import java.util.Locale

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var locationCallback: LocationCallback

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            when {
                it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> getLocation()
                it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> getLocation()
                else -> PermissionUtils.showPermissionSettings(
                    binding.root,
                    activity,
                    getString(R.string.location_permission_needed)
                )
            }
        }


    override fun init(savedInstanceState: Bundle?) {

        with(binding) {
            btnLocation.setOnClickListener {
                getLocationPermission()

            }
        }
    }


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)


    /**get Location Permission*/
    private fun getLocationPermission() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    /**get Location*/
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val client = LocationServices.getFusedLocationProviderClient(mContext)
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, 0).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.firstOrNull {
                    it != null
                }?.let {
                    client.removeLocationUpdates(locationCallback)
                    setLocation(it)
                }
            }
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    /**set Location*/
    private fun setLocation(location: Location) {

        Log.d(TAG, "lat: ${location.latitude} long: ${location.longitude} ")

        Log.d(TAG, "setLocation: ${getPlaceFromLatLong(mContext, location)}")

    }


    /**
     * this function returns the place name from (latitude,longitude)
     * @param[mContext] current context
     * @param[latLng] address latLng
     * @return[String] place name
     */
    fun getPlaceFromLatLong(mContext: Context, latLng: Location?): String {
        val longitude = latLng?.longitude
        val latitude = latLng?.latitude
        var place = ""
        val geocoder = Geocoder(mContext, Locale.getDefault())
        var addressList: List<Address>? = null
        try {
            addressList = latitude?.let {
                longitude?.let { it1 ->
                    geocoder.getFromLocation(it, it1, 5)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (!addressList.isNullOrEmpty()) {
                val bestAddress = addressList.firstOrNull {
                    !it.getAddressLine(0).contains("+", true) &&
                            !it.getAddressLine(0).startsWith("Unnamed Road", true)
                }
                place =
                    if (bestAddress != null) bestAddress.getAddressLine(0) else addressList.first()
                        .getAddressLine(0)

            }

        }
        return place
    }


}