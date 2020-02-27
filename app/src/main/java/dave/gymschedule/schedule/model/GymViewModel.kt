package dave.gymschedule.schedule.model

import dave.gymschedule.settings.model.GymLocation

data class GymViewModel(
        val location: GymLocation,
        val events: List<GymEventViewModel>
)