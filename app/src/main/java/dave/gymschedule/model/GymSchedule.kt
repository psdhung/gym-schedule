package dave.gymschedule.model

import com.google.gson.annotations.SerializedName


data class GymSchedule(
        val centreId: Int,

        @SerializedName("classes")
        val events: List<GymEvents>
)