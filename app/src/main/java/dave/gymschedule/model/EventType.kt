package dave.gymschedule.model

enum class EventType(val eventName: String, val eventTypeId: Int) {
    OTHER("Other", -1),
    FITNESS_CLASSES("Fitness Classes", 2),
    SPORTS_AND_RECREATION("Sports and Recreation", 3),
    POOL_ACTIVITIES("Pool Activities", 4),
    TEEN_AND_FAMILY_ACTIVITIES("Teen & Family Activities", 5);

    companion object {
        fun getEventTypeFromId(id: Int) : EventType {
            for (category in values()) {
                if (category.eventTypeId == id) {
                    return category
                }
            }

            return OTHER
        }
    }
}
