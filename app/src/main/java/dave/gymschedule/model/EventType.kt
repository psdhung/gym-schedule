package dave.gymschedule.model

enum class EventType(val eventName: String, val eventTypeId: Int) {
    OTHER("Other", -1),
    POOL_ACTIVITIES("Pool Activities", 4);

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
