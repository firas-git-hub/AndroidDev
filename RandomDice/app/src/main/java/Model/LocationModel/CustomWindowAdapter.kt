package Model.LocationModel

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.randomdice.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomWindowAdapter public constructor(contextArg: Context) : GoogleMap.InfoWindowAdapter {

    private var context: Context = contextArg
    private var popWindow: View = LayoutInflater.from(context).inflate(R.layout.marker_click_popup_window, null)

    private fun renderWindow(marker: Marker, view: View){
        view.findViewById<TextView>(R.id.markerPopupText).text = "Coords: ${marker.position}"
    }

    override fun getInfoContents(p0: Marker): View? {
        renderWindow(p0, popWindow)
        return popWindow
    }

    override fun getInfoWindow(p0: Marker): View? {
        renderWindow(p0, popWindow)
        return popWindow
    }
}