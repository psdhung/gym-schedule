package dave.gymschedule.settings.model

enum class EventType(val eventName: String, val eventTypeId: Int) {
    FITNESS_CLASSES("Fitness Classes", 2),
    SPORTS_AND_RECREATION("Sports and Recreation", 3),
    POOL_ACTIVITIES("Pool Activities", 4),
    TEEN_AND_FAMILY_ACTIVITIES("Teen & Family Activities", 5),
    OTHER("Other", -1);

    companion object {
        fun getEventTypeFromId(id: Int) : EventType {
            return values().firstOrNull { it.eventTypeId == id } ?: OTHER
        }
    }
}
