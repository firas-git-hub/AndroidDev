package com.example.randomdice

import Model.LocationModel.CustomWindowAdapter
import Model.MapServices
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.os.HandlerCompat.postDelayed
import androidx.loader.content.AsyncTaskLoader
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.net.ContentHandlerFactory

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    interface locationRequestCallback {
        fun onLocationFetched(long: String, lat: String)
        fun onLocationError(error: String)
    }

    val fineLocationPermissionRQ = 101
    val coarseLocationPermissionRQ = 102
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private lateinit var coordsTextView: TextView
    private lateinit var saveCoordsButt: Button
    private lateinit var map: GoogleMap
    var longitude = ""
    var latitude = ""
    var coordsFetched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mapView = findViewById(R.id.mapView)
        coordsTextView = findViewById(R.id.coordsTextView)
        saveCoordsButt = findViewById(R.id.saveCoordsButt)

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
            getLocation(object: locationRequestCallback{
                override fun onLocationFetched(long: String, lat: String) {
                    longitude = long
                    latitude = lat
                    coordsTextView.text = "Last location : long: $longitude / lat: $latitude"
                    coordsFetched = true
                }

                override fun onLocationError(error: String) {
                    Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                }
            })
        }

        saveCoordsButt.setOnClickListener {
            if (longitude != "" && latitude != "") {
                Model.LocationModel.LocationServices.postMarkers(longitude, latitude)
                Toast.makeText(
                    applicationContext,
                    "Coordinates $longitude $latitude have been saved",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Press the text to fetch coordinates first, then try to save",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getLocation(locationCallBack: locationRequestCallback){
        try {
            if(MapServices.checkPermission(this)) {
                coordsTextView.isClickable = false
                coordsTextView.text = "waiting for task to end"
                var locationtask = fusedLocationClient.lastLocation
                locationtask.addOnCompleteListener {
                    if (locationtask.isSuccessful) {
                        val long = locationtask.getResult().longitude.toString()
                        val lat = locationtask.getResult().latitude.toString()
                        locationCallBack.onLocationFetched(long.toString(), lat.toString())
                        coordsTextView.isClickable = true
                    }
                }
            }
        } catch (e: SecurityException){
            Log.e("Security exception", e.message!!)
            locationCallBack.onLocationError(e.message!!)
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
        map = p0
        Model.LocationModel.LocationServices.getMarkers(map)
        if(MapServices.checkPermission(this)){
            p0.isMyLocationEnabled = true
        }
        map.setInfoWindowAdapter(CustomWindowAdapter(this))
        map.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(p0: Marker) {
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setPositiveButton("Delete",
                    DialogInterface.OnClickListener { dialog, id ->
                        p0.remove()
                    })
                setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                setMessage("Are you sure you want to remove this Pin?")
                setTitle("Remove Pin")
            }
            builder.create()
        }
        alertDialog?.show()
    }
}
