package Model

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.randomdice.APIService
import com.example.randomdice.DiceAdapter
import com.example.randomdice.Results
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RandomDiceService {

    companion object {

        var JsonResults = ""

        public fun postResults(result: String) {
            val retroFit = Retrofit.Builder()
                .baseUrl("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/")
                .build()

            val service = retroFit.create(APIService::class.java)
            val jsonObject = JSONObject()
            jsonObject.put("result", result)

            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.addResults(requestBody)

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

        fun getResults(diceRecyclerView: RecyclerView) {
            val retroFit = Retrofit.Builder()
                .baseUrl("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retroFit.create(APIService::class.java)

            val call: Call<List<DiceResult>> = service.getResults()

            call.enqueue(object : Callback<List<DiceResult>?> {
                override fun onResponse(
                    call: Call<List<DiceResult>?>,
                    response: Response<List<DiceResult>?>
                ) {
                    val responseBody = response.body()!!
                    for(myData in responseBody){
                        Results.results.add(DiceResult(myData.roll, myData.id))
                    }
                    updateViewItem(diceRecyclerView)
                }

                override fun onFailure(call: Call<List<DiceResult>?>, t: Throwable) {
                    Log.e("RETROFIT_ERROR", call.toString())
                }
            })
        }

        fun deleteResult(adapter: DiceAdapter, resultId: Int, resultPosition: Int){
            val retrofit = Retrofit.Builder()
                .baseUrl("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(APIService::class.java)

            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = service.deleteResult(resultId.toString())

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        Log.d("Deleted", "Deleted from API")
                        Results.results.removeAt(resultPosition)
                        adapter.notifyDataSetChanged()

                    } else {

                        Log.e("RETROFIT_ERROR", response.code().toString())

                    }
                }
            }
        }

        fun updateViewItem(diceRecyclerView: RecyclerView){
            diceRecyclerView.adapter!!.notifyDataSetChanged()
        }
    }
}