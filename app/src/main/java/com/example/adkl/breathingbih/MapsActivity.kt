package com.example.adkl.breathingbih

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.adkl.breathingbih.model.BBIHPlace
import com.example.adkl.breathingbih.service.NonSmokingPlacesService
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Tasks.await
import kotlinx.android.synthetic.main.activity_maps.*

@Suppress("PrivatePropertyName")
class MapsActivity : AppCompatActivity(),
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnPoiClickListener,
        GoogleMap.OnMarkerClickListener {

    private val SARAJEVO_LAT_LNG = LatLng(43.8563, 18.4131)
    private val CURRENT_LOCATION_REQUEST_ID = 1
    private val DEFAULT_MAP_ZOOM = 15f
    private val DETAILS_MAP_ZOOM = 18f

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mGeoDataClient: GeoDataClient
    private var mLocationPermissionGranted: Boolean = false

    private val mLocationCallback: LocationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val it = locationResult!!.lastLocation
            updateMap(LatLng(it.latitude, it.longitude))
            mFusedLocationProviderClient.removeLocationUpdates(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mGeoDataClient = Places.getGeoDataClient(this)

        initializeViews()
    }

    private fun initializeViews() {
        val autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        autocompleteFragment.setOnPlaceSelectedListener(
                object : PlaceSelectionListener {
                    override fun onPlaceSelected(place: Place?) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place!!.latLng, DETAILS_MAP_ZOOM))
                    }

                    override fun onError(p0: Status?) {
                        Log.e("AutoCompleteFragment", p0.toString())
                    }
                }
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnPoiClickListener(this)
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)

        retrieveLocationPermission()
        setInitialLocation()
        updateMapWithCurrentLocation() // At least try :D

        updateMapWithNonSmokingPlaces()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val place: BBIHPlace = marker!!.tag as BBIHPlace

        clearPlaceDetails()
        fillPlaceDetails(place)

        place_details_cardview!!.visibility = View.VISIBLE
        return false
    }

    private fun fillPlaceDetails(place: BBIHPlace) {
        non_smoking_place_title_tv.text = place.name
        non_smoking_place_description_tv.text = place.typeString()
    }

    private fun clearPlaceDetails() {
        non_smoking_place_title_tv.text = ""
        non_smoking_place_description_tv.text = ""
    }

    private fun updateMapWithNonSmokingPlaces() {
        NonSmokingPlacesService.getNonSmokingPlaces().forEach {
            showNonSmokingArea(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateMapWithCurrentLocation() {
        if (mLocationPermissionGranted) {
            mFusedLocationProviderClient.requestLocationUpdates(
                    LocationRequest()
                            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                            .setInterval(10 * 1000)
                            .setFastestInterval(1 * 1000),
                    mLocationCallback,
                    null
            )
        }
    }

    private fun setInitialLocation() {
        updateMap(SARAJEVO_LAT_LNG)
    }

    private fun updateMap(location: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_MAP_ZOOM))
    }

    private fun showNonSmokingArea(place: BBIHPlace) {
        val location = LatLng(place.lat, place.lng)

        val icon = BitmapDescriptorFactory.defaultMarker() // TODO Add a custom icon
        mMap.addMarker(MarkerOptions()
                .position(location)
                .icon(icon)
                .title(place.name)
        ).tag = place
    }

    private fun retrieveLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
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

    override fun onPoiClick(poi: PointOfInterest?) {
        Log.i("POI_CLICK", poi!!.name + " - " + poi.latLng.latitude + " | " + poi.latLng.longitude)
    }


    override fun onMapClick(latLng: LatLng?) {
        place_details_cardview!!.visibility = View.INVISIBLE
    }
}
