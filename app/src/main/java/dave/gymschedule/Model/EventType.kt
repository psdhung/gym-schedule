package dave.gymschedule.Model

enum class EventType(val eventTypeId: Int) {
    OTHER(-1),
    POOL_ACTIVITIES(4);

    companion object {
        fun getCategoryFromId(id: Int) : EventType {
            for (category in EventType.values()) {
                if (category.eventTypeId == id) {
                    return category
                }
            }

            return OTHER
        }
    }
}
