package dave.gymschedule.model

import com.google.gson.annotations.SerializedName

data class GymEvent(
        @SerializedName("className")
        val name: String,

        @SerializedName("classTypeId")
        val eventType: Int,

        @SerializedName("titleDetail")
        val details: String?,

        @SerializedName("startTime")
        val startTime: String,

        @SerializedName("endTime")
        val endTime: String,

        @SerializedName("room")
        val location: String
)