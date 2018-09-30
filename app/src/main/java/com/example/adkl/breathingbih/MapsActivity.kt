package com.example.adkl.breathingbih

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val SARAJEVO_LATLNG = LatLng(43.8563, 18.4131)
    private val CURRENT_LOCATION_REQUEST_ID = 220495

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLocationPermissionGranted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        retrieveLocationPermission()
        setInitialLocation()
        updateMapWithCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun updateMapWithCurrentLocation() {
        if (mLocationPermissionGranted) {
            mFusedLocationProviderClient.lastLocation.addOnCompleteListener {
                updateMap(LatLng(it.result.longitude, it.result.latitude))
            }
        }
    }

    private fun setInitialLocation() {
        updateMap(SARAJEVO_LATLNG)
    }

    private fun updateMap(location: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        mMap.addMarker(MarkerOptions().position(location))
    }

    private fun retrieveLocationPermission() {
        if ( ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            mLocationPermissionGranted = true
        }
        else {
            ActivityCompat.requestPermissions(
                    this,
                    Array(1) { Manifest.permission.ACCESS_FINE_LOCATION },
                    CURRENT_LOCATION_REQUEST_ID)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CURRENT_LOCATION_REQUEST_ID &&
                grantResults.count() > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        }
    }
}
