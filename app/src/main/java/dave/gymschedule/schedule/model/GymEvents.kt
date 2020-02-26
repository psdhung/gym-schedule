package dave.gymschedule.schedule.model

import com.google.gson.annotations.SerializedName

data class GymEvents(
        @SerializedName("classes")
        val events: List<GymEvent>
)