package Model.LocationModel

import Model.DiceResult
import Model.RandomDiceService
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.randomdice.APIService
import com.example.randomdice.LocationAPIService
import com.example.randomdice.Results
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationServices {
    companion object {
        var JsonResults = ""

        public fun postMarkers(longitude: String, latitude: String) {
            val retroFit = Retrofit.Builder()
                .baseUrl("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/")
                .build()

            val service = retroFit.create(LocationAPIService::class.java)
            val jsonObject = JSONObject()
            jsonObject.put("marker_longitude", longitude)
            jsonObject.put("marker_latitude", latitude)

            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.addMarker(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        // Convert raw JSON to pretty JSON using GSON library
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(
                            JsonParser.parseString(
                                response.body()
                                    ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                            )
                        )

                        Log.d("Pretty Printed JSON :", prettyJson)

                    } else {

                        Log.e("RETROFIT_ERROR", response.code().toString())

                    }
                }
            }
        }

        fun getMarkers(map: GoogleMap) {
            val retroFit = Retrofit.Builder()
                .baseUrl("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retroFit.create(LocationAPIService::class.java)

            val call: Call<List<Marker>> = service.getMarkers()

            call.enqueue(object : Callback<List<Marker>?> {
                override fun onResponse(
                    call: Call<List<Marker>?>,
                    response: Response<List<Marker>?>
                ) {
                    val responseBody = response.body()!!
                    for (myData in responseBody) {
                        MarkersList.markerList.add(
                            map.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        myData.marker_latitude.toDouble(),
                                        myData.marker_longitude.toDouble()
                                    )
                                ).title("Marker")
                                    .snippet(" Lon: ${myData.marker_longitude} Lat: ${myData.marker_latitude}")
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<List<Marker>?>, t: Throwable) {
                    Log.e("RETROFIT_ERROR", call.toString())
                }
            })
        }
    }
}