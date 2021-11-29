package com.example.sample_app.ui.home.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sample_app.R
import com.example.sample_app.databinding.FragmentHomeBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import java.lang.ClassCastException
import java.lang.Exception
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.model.LatLng
import java.util.*

import com.google.android.gms.maps.model.BitmapDescriptorFactory

import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import com.example.sample_app.app.App
import com.example.sample_app.db.Address

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.gson.Gson


class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentHomeBinding
    private val PERMISSION_REQUEST = 1188
    val REQUEST_LOCATION = 299

    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLastLocation: Location? = null
    var mapFragment: SupportMapFragment? = null
    private var alert: AlertDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)

        binding.btnChoose.setOnClickListener {
            var address = Address()
            address.address = binding.textViewAddress.text.toString()
            address.lat = mLastLocation!!.latitude
            address.lng = mLastLocation!!.longitude
            App.mAppDatabase!!.getAddressDao().insert(address)
            Toast.makeText(requireContext(),"Saved Successfully!!!",Toast.LENGTH_LONG).show()

            Log.e(
                "Nive ",
                "onCreateView:Home " + Gson().toJson(
                    App.mAppDatabase!!.getAddressDao().getAddress()
                )
            )

        }

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        locationPermissionCheck()
    }


    fun locationPermissionCheck() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                requestLocationUpdates()
            } else {
                checkLocationPermission()
            }
        } else {
            showGPSDisabledAlert()
        }
    }


    private fun showGPSDisabledAlert() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage(getString(R.string.gps_is_disabled))
            .setCancelable(false)
            .setTitle(getString(R.string.enable_gps))
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialog, id ->
                checkGps()
                dialog.dismiss()
            }
        val alert = alertDialogBuilder.create()
        alert.show()
    }


    private fun checkGps() {
        val settingsBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        settingsBuilder.setAlwaysShow(true)
        val result =
            LocationServices.getSettingsClient(requireActivity())
                .checkLocationSettings(settingsBuilder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(
                    ApiException::class.java
                )
                // All location settings are satisfied. The client can initialize location
                // requests here.
                requestLocationUpdates()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                requireActivity(),
                                REQUEST_LOCATION
                            )
                        } catch (e: SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->                             // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        showGPSDisabledAlert()
                }
            }
        }
    }


    fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            val permissionList = ArrayList<String>()
            if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            Log.e("Nive ", "checkLocationPermission:$permissionList")
            if (permissionList.size > 0) {
                val permissionArray = arrayOfNulls<String>(permissionList.size)
                permissionList.toArray(permissionArray)
                requestPermissions(
                    permissionArray,
                    PERMISSION_REQUEST
                )
            }
        }
    }


    private fun requestLocationUpdates() {
        try {
            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 8000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            getLastLocation(locationRequest)
            Log.e("Location", "Requesting location updates")
        } catch (e: Exception) {
            Log.e("Location", "Lost location permission. Could not request updates. $e")
        }
    }


    private fun getLastLocation(locationRequest: LocationRequest) {
        try {
            mFusedLocationClient!!.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLastLocation = task.result
                        Log.e("Nive ", "getLastLocation:check  ${mLastLocation}")
                        val latLng = LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f))
                        binding.centerFrameDrop.visibility = View.VISIBLE
                        getAddress(latLng)
                    } else {
                        Log.e("TAG", "Failed to get location.")
                        mFusedLocationClient!!.requestLocationUpdates(
                            locationRequest,
                            setLocationRequestCallBack(), Looper.myLooper()
                        )
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e("TAG", "Lost location permission.$unlikely")
        }
    }


    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    private fun getAddress(mLastLocation: LatLng) {


        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addressList =
                geocoder.getFromLocation(mLastLocation.latitude, mLastLocation.longitude, 1)
            Log.e("Nive ", "getAddress: ${addressList}")
            if (addressList != null && addressList.size > 0) {
                val obj = addressList[0]
                binding.textViewAddress.text = obj.getAddressLine(0)
            }
        } catch (e: Exception) {
        }


    }


    private fun setLocationRequestCallBack(): LocationCallback? {
        Log.e(
            "TAG",
            "onLocationResult:setLocationRequestCallBack  mLocationCallback before $mLocationCallback"
        )
        if (mLocationCallback == null) {
            Log.e(
                "TAG",
                "onLocationResult:setLocationRequestCallBack  mLocationCallback before null "
            )
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    Log.e(
                        "TAG",
                        "onLocationResult:setLocationRequestCallBack $locationResult"
                    )
                    if (locationResult != null) {
                        super.onLocationResult(locationResult)
                        if (mLastLocation == null) {
                            val mLastLocation = locationResult.lastLocation
                            Log.e("Nive ", "onLocationResult:cehck ${mLastLocation}")
                        }
                        mFusedLocationClient!!.removeLocationUpdates(this)
                    } else {
                        Log.e("Giri ", "onLocationResult: null")
                    }
                }
            }
        }
        return mLocationCallback
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var i = 0
            val len = permissions.size
            while (i < len) {
                val permission = permissions[i]
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        permissions[i]
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    val showRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            permission
                        )
                    openLocationEnablePopup(
                        showRationale,
                        "Enable Location Permissions to access the app"
                    )
                } else {
                    Log.e("Nive ", "onRequestPermissionsResult:granted ")
                    requestLocationUpdates()
                }
                i++
            }
        }
    }

    private fun openLocationEnablePopup(showRationale: Boolean, message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(
                getString(R.string.enable)
            ) { dialog, id ->
                //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                if (!showRationale) {
                    /*if (!showRationale) { user also CHECKED "never ask again" }*/
                    // HERE USER CHECKED "never ask again"
                    alert?.dismiss()
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    )
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    locationPermissionCheck()
                }
            }
        alert = builder.create()
        if (!alert?.isShowing!!) {
            alert?.show()
        }
    }


    override fun onMapReady(p0: GoogleMap) {

        mMap = p0
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        mMap.isMyLocationEnabled = false


        mMap.setOnCameraIdleListener {
            getAddress(mMap.cameraPosition.target)
        }

    }
}