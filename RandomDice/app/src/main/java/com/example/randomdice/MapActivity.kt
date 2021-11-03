package com.example.randomdice

import Model.MapServices
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.HandlerCompat.postDelayed
import androidx.loader.content.AsyncTaskLoader
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.net.ContentHandlerFactory

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    val fineLocationPermissionRQ = 101
    val coarseLocationPermissionRQ = 102
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private lateinit var coordsTextView: TextView
    var location = "empty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mapView = findViewById(R.id.mapView)
        coordsTextView = findViewById(R.id.coordsTextView)

        if(MapServices.checkPermission(this)){
            mapView.getMapAsync(this)
            mapView.onCreate(savedInstanceState)
        } else {
            MapServices.requestPermission(this, fineLocationPermissionRQ, coarseLocationPermissionRQ)
        }
        bindOnClicks()
    }

    private fun bindOnClicks(){
        coordsTextView.setOnClickListener{
            coordsTextView.text = location
        }
    }

    private fun getLocation(){
        try {
            if(MapServices.checkPermission(this)) {
                coordsTextView.isClickable = false
                coordsTextView.text = "waiting for task to end"
                var locationtask = fusedLocationClient.lastLocation
                if (locationtask.isSuccessful) {
                    val long = locationtask.getResult().longitude.toString()
                    val lat = locationtask.getResult().latitude.toString()
                    location = "Last location : long: " + long + " / lat: " + lat
                    coordsTextView.isClickable = true
                    coordsTextView.text = location
                }
            }

        } catch(e: SecurityException){
            Log.e("Security exception", e.message!!)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        mapView.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mapView = findViewById(R.id.mapView)
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.addMarker(MarkerOptions().position(LatLng(20.0, 20.0)).title("Marker"))
        if(MapServices.checkPermission(this)){
            p0.isMyLocationEnabled = true
            getLocation()
        }
    }
}
