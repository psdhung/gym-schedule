package dave.gymschedule.repository

import android.util.Log
import dave.gymschedule.model.EventType
import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.service.YmcaService
import io.reactivex.Single
import java.util.Calendar
import java.util.Locale

class GymScheduleRepository(private val ymcaService: YmcaService) {

    companion object {
        private val TAG = GymScheduleRepository::class.java.simpleName
    }

    fun getGymEventsViewModelSingle(date: Calendar): Single<List<GymEventViewModel>> {
        val startDateTime = "${getFormattedDateString(date)}+12:00:00+AM"
        val endDateTime = "${getFormattedDateString(date)}+11:59:59+PM"
        Log.d(TAG, "getting gym events for date $date")
        val gymScheduleSingle = ymcaService.getGymSchedule(startDateTime, endDateTime)
        return gymScheduleSingle.map { gym ->
            val gymEventViewModels = mutableListOf<GymEventViewModel>()

            gym.events.forEach {gymEvents ->
                gymEvents.events.forEach { gymEvent ->
                    gymEventViewModels.add(GymEventViewModel(
                            gymEvent.name,
                            EventType.getEventTypeFromId(gymEvent.eventType),
                            gymEvent.details,
                            gymEvent.startTime,
                            gymEvent.endTime,
                            gymEvent.location,
                            gymEvent.description,
                            gymEvent.fee,
                            gymEvent.childMinding,
                            gymEvent.ageRange,
                            gymEvent.registrationType
                    ))
                }
            }

            gymEventViewModels
        }
    }

    private fun getFormattedDateString(calendar: Calendar): String {
        return String.format(Locale.getDefault(), "%d-%d-%d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH))
    }

}