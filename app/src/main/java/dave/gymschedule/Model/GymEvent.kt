package dave.gymschedule.Model

data class GymEvent(val name: String,
                    val eventType: EventType,
                    val details: String,
                    val startTime: String,
                    val endTime: String)