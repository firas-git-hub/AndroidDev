package com.example.randomdice

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class MapActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val getLocButton: Button = findViewById<Button>(R.id.getLocBut)
        val locTextView: TextView = findViewById<TextView>(R.id.locTextView)

        getLocButton.setOnClickListener{
            locTextView.setText(getLocation())
        }

    }

    private fun getLocation(): String {
        var locationRes = ""
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return "Permission denied"
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                locationRes = location.toString()
                Log.d("location",location.toString())
            }
        return locationRes
    }

}