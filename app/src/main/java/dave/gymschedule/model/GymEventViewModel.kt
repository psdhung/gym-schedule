package dave.gymschedule.model

data class GymEventViewModel(
        val name: String,
        val eventType: EventType,
        val details: String?,
        val startTime: String,
        val endTime: String,
        val location: String
)