package Model
import com.google.gson.JsonObject
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface APIService {
    @POST("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/results")
    suspend fun addResults(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/results")
    fun getResults(): Call<List<DiceResult>>

    @DELETE("https://616004fdfaa03600179fb844.mockapi.io/api/RandomDice/results/{id}")
    suspend fun deleteResult(@Path("id") resultId: String): Response<ResponseBody>
}