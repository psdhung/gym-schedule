package dave.gymschedule.schedule.model

import com.google.gson.annotations.SerializedName
import dave.gymschedule.schedule.model.GymEvents


data class GymSchedule(
        val centreId: Int,

        @SerializedName("classes")
        val events: List<GymEvents>
)