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
        val location: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("ageRange")
        val ageRange: String,

        @SerializedName("fee")
        val fee: String,

        @SerializedName("instructor")
        val instructor: String,

        @SerializedName("registrationType")
        val registrationType: String,

        @SerializedName("registrationInstructions")
        val registrationInstructions: String,

        @SerializedName("childminding")
        val childMinding: Boolean
)