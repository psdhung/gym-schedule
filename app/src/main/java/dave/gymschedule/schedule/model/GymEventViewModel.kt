package dave.gymschedule.schedule.model

import dave.gymschedule.settings.model.EventType

data class GymEventViewModel(
        val name: String,
        val eventType: EventType,
        val details: String?,
        val startTime: String,
        val endTime: String,
        val location: String,
        val description: String,
        val fee: String,
        val childMinding: Boolean,
        val ageRange: String,
        val registration: String,
        var isExpanded: Boolean = false
)