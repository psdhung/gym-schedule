package dave.gymschedule.schedule.model

import com.google.gson.annotations.SerializedName
import dave.gymschedule.schedule.model.GymEvent

data class GymEvents(
        @SerializedName("classes")
        val events: List<GymEvent>
)