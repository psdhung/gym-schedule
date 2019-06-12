package dave.gymschedule.repository

import android.util.Log
import dave.gymschedule.model.EventType
import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.service.YmcaService
import io.reactivex.Single
import retrofit2.Retrofit
import java.util.Calendar
import java.util.Locale

class GymScheduleRepository(private val retrofit: Retrofit) {

    companion object {
        private val TAG = GymScheduleRepository::class.java.simpleName
    }

    fun getGymEventsViewModelSingle(date: Calendar): Single<List<GymEventViewModel>> {
        val ymcaService = retrofit.create(YmcaService::class.java)

        val startDateTime = "${getFormattedDateString(date)}+12:00:00+AM"
        val endDateTime = "${getFormattedDateString(date)}+11:59:59+PM"
        Log.d(TAG, "getting gym events for date $date")
        val gymEventsSingle = ymcaService.getGymSchedule(startDateTime, endDateTime)
        return gymEventsSingle.map { gym ->
            val gymEvents = mutableListOf<GymEventViewModel>()

            gym.events.forEach {
                it.events.forEach { gymEvent ->
                    gymEvents.add(GymEventViewModel(
                            gymEvent.name,
                            EventType.getEventTypeFromId(gymEvent.eventType),
                            gymEvent.details,
                            gymEvent.startTime,
                            gymEvent.endTime,
                            gymEvent.location
                    ))
                }
            }

            gymEvents
        }
    }

    private fun getFormattedDateString(calendar: Calendar): String {
        return String.format(Locale.getDefault(), "%d-%d-%d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH))
    }
}