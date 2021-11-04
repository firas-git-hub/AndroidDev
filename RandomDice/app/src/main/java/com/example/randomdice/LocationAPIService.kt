package com.example.randomdice

import Model.LocationModel.Marker
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface LocationAPIService {
    @POST("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/markers")
    suspend fun addMarker(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/markers")
    fun getMarkers(): Call<List<Marker>>

    @DELETE("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/markers/{id}")
    suspend fun deleteMarker(@Path("id") resultId: String): Response<ResponseBody>
}