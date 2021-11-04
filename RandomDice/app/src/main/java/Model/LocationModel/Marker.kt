package Model.LocationModel

import com.google.gson.annotations.SerializedName

data class Marker(@SerializedName("marker_id") var marker_id: Int,
                  @SerializedName("marker_longitude") var marker_longitude: String,
                  @SerializedName("marker_latitude") var marker_latitude: String)