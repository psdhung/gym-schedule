package dave.gymschedule.model

import com.google.gson.annotations.SerializedName

data class GymEvents(
        @SerializedName("classes")
        val events: List<GymEvent>
)