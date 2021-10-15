package Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DiceResult(@SerializedName("result") val roll: String, val id: Int) : Serializable
